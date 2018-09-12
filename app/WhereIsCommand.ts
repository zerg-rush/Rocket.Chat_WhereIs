import {HttpStatusCode, IHttp, IModify, IRead} from '@rocket.chat/apps-ts-definition/accessors';
import {ISlashCommand, SlashCommandContext} from '@rocket.chat/apps-ts-definition/slashcommands';

import {WhereIsApp} from './WhereIsApp';
import {displayLocationResponse} from './DisplayLocationResponse';

export class WhereIsCommand implements ISlashCommand {
    public command = 'whereis';

    /* Permission value required for the user to have to see/use it. */
    public permission = 'whereis';

    public i18nParamsExample = 'WhereIs_Command_Example';
    public i18nDescription = 'WhereIs_Command_Description';

    /* This command does not provide preview results. */
    public providesPreview = false;

    public whereIsRequiredRoles = ['admin', 'whereis', 'whereis-details', 'whereis-admin'];
    public whereIsDetailsRequiredRoles = ['admin', 'whereis-details', 'whereis-admin'];

    public regexUser = RegExp('^[0-9a-zA-Z-_.]+$');
    public regexMac = RegExp('^([0-9a-f]{1,2}[\\.:-]){5}([0-9a-f]{1,2})$', 'i');
    public regexIp = RegExp('^(?!0)(?!.*\\.$)((1?\\d?\\d|25[0-5]|2[0-4]\\d)(\\.|$)){4}$', 'i');

    public introMessage = 'Hi, my name is WhereIs v' + this.app.getVersion() +
        ', and I am bot created to help you to locate other users at company facilities.\n' +
        '\n' +
        'I can provide you location data for specified Rocket.Chat users, and devices with specific IP and MAC addresses.' +
        ' Just call me (using "/whereis") and provide me the name of user which you are looking for, or IP/MAC address.\n\n';

    public syntaxMessage =
        'Usage scenarios:\n' +
        '             /whereis user_nick   - displays location data for user with specified nick,\n' +
        '   /whereis IP_ADDR    - displays location data for device with specified IP address,\n' +
        '   /whereis MAC_ADDR   - displays location data for device with specified MAC address,\n' +
        '   /whereis [help]     - displays this detailed information about myself\n' +
        '\n' +
        'See following examples:\n' +
        '   /whereis jan\n' +
        '   /whereis 192.168.1.2\n' +
        '   /whereis 01-23-45-67-89-AB\n';

    public notAuthorized = 'Sorry, you are not authorized to access this information :( \n';

    public serverError = 'Sorry, service is not available right now (please contact administrator or try later)\n';

    //public backendHost = 'http://192.168.253.95:8080/';
    //public backendUrl = this.backendHost + 'whereis';

    public backendHost = '';
    public backendUrl = '';

    public BACKEND_TIMEOUT = 1000;

    constructor(private readonly app: WhereIsApp) {
    }

    checkMode(args: string[], authorized): string {
        let result = 'HELP';
        if (args.length == 1) {
            let arg = args[0].toLowerCase();
            if (this.regexMac.test(arg)) {
                result = 'MAC';
            }

            if (result == 'HELP' && this.regexIp.test(arg)) {
                result = 'IP';
            }

            if (result == 'HELP' && this.regexUser.test(arg)) {
                result = 'USER';
            }

            if (arg == 'help' || arg == '/h' || arg == '-h'
                || arg == '--help') {
                result = 'HELP';
            }
        }
        if (!authorized && (result == 'USER' || result == 'IP' || result == 'MAC')) {
            result = 'NOT_AUTHORIZED';
        }
        return result;
    }

    isAuthorized(senderRoles: string[]): boolean {
        let result = false;
        senderRoles.forEach(role => {
            result = result || this.whereIsRequiredRoles.includes(role);
        });
        return result;
    }

    isAuthorizedForDetails(senderRoles: string[]): boolean {
        let result = false;
        senderRoles.forEach(role => {
            result = result || this.whereIsDetailsRequiredRoles.includes(role);
        });
        return result;
    }

    async displayMessage(read, modify, room, sender, message): Promise<void> {
        const backendUrl = await read.getEnvironmentReader().getSettings().getValueById('WhereIs_Backend_Service_URL')
        const botAvatarUrl = backendUrl.replace("/api/whereis", "/images/whereis.jpg");

        const builder = modify.getCreator().startMessage()
            .setSender(sender)
            .setRoom(room)
            .setUsernameAlias('Where Is')
            .setAvatarUrl(botAvatarUrl)
            .setText(message);
        await modify.getNotifer().notifyUser(sender, builder.getMessage());
    }

    public async executor(context: SlashCommandContext, read: IRead, modify: IModify, http: IHttp): Promise<void> {
        this.backendUrl = await read.getEnvironmentReader().getSettings().getValueById('WhereIs_Backend_Service_URL');
        this.backendHost = this.backendUrl.split('://')[1].split('/')[0];

        const command = context.getArguments().join(' ');

        let sender = context.getSender();
        let senderRoles = sender.roles;
        let room = context.getRoom();

        const args = context.getArguments();
        let authorized = this.isAuthorized(senderRoles);
        let authorizedForDetails = this.isAuthorizedForDetails(senderRoles);

        let mode = this.checkMode(args, authorized);

        if (!authorized && (mode == 'USER' || mode == 'IP' || mode == 'MAC')) {
            this.displayMessage(read, modify, room, sender, this.notAuthorized);
            mode = 'NOT_AUTHORIZED';
        }

        let id = '';
        let userExists = true;
        let fullname = '';
        let avatarUrl = '';
        this.app.getLogger().error('mode = ' + mode);
        switch (mode) {
            case 'USER':
                id = args[0].toLowerCase();
                const user = await read.getUserReader().getByUsername(id);
                userExists = user != null;
                if (userExists) {
                    fullname = user.name;
                    avatarUrl = 'avatar/' + id;
                } else {
                    mode = 'NOT_FOUND';
                    this.displayMessage(read, modify, room, sender, 'Sorry, user ' + id + ' does not exist!!!');
                    this.app.getLogger().error('Sorry, user ' + id + ' does not exist!!!');
                }
                break;
            case 'IP':
            case 'MAC':
                id = args[0].toLowerCase();
                break;
            case 'NOT_AUTHORIZED':
                if (args.length > 0) {
                    id = args[0].toLowerCase();
                }
                fullname = sender.username;
                this.displayMessage(read, modify, room, sender, this.notAuthorized);
                break;
            case 'HELP':
                this.displayMessage(read, modify, room, sender, this.introMessage + this.syntaxMessage);
                return;
                break;
            case 'SYNTAX':
                this.displayMessage(read, modify, room, sender, this.syntaxMessage);
                return;
                break;
        }

        let errorMessage = '';

        const arg = {
            import_format: 'plain_text',
        };

        if (mode != 'HELP') {
            const postRequest = {
                data: {
                    command: command,
                    mode: mode,
                    id: id,
                    exists: userExists,
                    fullname: fullname,
                    avatarUrl: avatarUrl,
                    senderRoles: senderRoles,
                    timeout: this.BACKEND_TIMEOUT
                },
                content: command,
                headers: {
                    'Content-Type': 'application/json',
                    'user-agent': 'Rocket.Chat WhereIs bot v' + this.app.getVersion()
                }
            };
            const response = await http.post(this.backendUrl, postRequest);

            if (response != undefined && response.statusCode === HttpStatusCode.OK) {
                await displayLocationResponse(response.data, context, read, modify);
            } else {
                if (response != undefined) {
                    errorMessage = response.statusCode + ', ' + response.data.toString();
                } else {
                    errorMessage = '(no response)';
                }
                this.app.getLogger().error('Backend server error: ' + errorMessage);
                this.displayMessage(read, modify, room, sender, this.serverError);
            }
        }
    }
}
