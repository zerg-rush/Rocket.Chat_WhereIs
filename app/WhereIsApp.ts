import { App } from '@rocket.chat/apps-engine/definition/App';
import {IAppInfo} from '@rocket.chat/apps-engine/definition/metadata';

import {IConfigurationExtend, IEnvironmentRead, ILogger} from '@rocket.chat/apps-engine/definition/accessors';

import {SettingType} from '@rocket.chat/apps-engine/definition/settings';

import {WhereIsCommand} from './WhereIsCommand';
import {SettingToHttpHeader} from './handlers/SettingToHttpHeader';

export class WhereIsApp extends App {

    constructor(info: IAppInfo, logger: ILogger) {
        super(info, logger);
    }

    public async initialize(configurationExtend: IConfigurationExtend, environmentRead: IEnvironmentRead): Promise<void> {
        await this.extendConfiguration(configurationExtend, environmentRead);
    }

    protected async extendConfiguration(configuration: IConfigurationExtend, environmentRead: IEnvironmentRead): Promise<void> {
        await configuration.settings.provideSetting({
            id: 'WhereIs_Bot',
            type: SettingType.STRING,
            packageValue: 'rocket.cat',
            required: true,
            public: false,
            i18nLabel: 'WhereIs_Bot',
            i18nDescription: 'WhereIs_Bot_Description',
        });

        await configuration.settings.provideSetting({
            id: 'WhereIs_Backend_Service_URL',
            type: SettingType.STRING,
            packageValue: 'http://whereis-backend:8080/api/whereis',
            //packageValue: 'http://192.168.1.3:8080/api/whereis',
            required: true,
            public: false,
            i18nLabel: 'WhereIs_Backend_Service_URL',
            i18nDescription: 'WhereIs_Backend_Service_URL_Description',
        });

        await configuration.settings.provideSetting({
            id: 'WhereIs_Backend_Service_Token',
            type: SettingType.STRING,
            packageValue: '12345678901234567890123456789012',
            required: true,
            public: false,
            i18nLabel: 'WhereIs_Backend_Service_Token',
            i18nDescription: 'WhereIs_Backend_Service_Token_Description',
        });

        configuration.http.providePreRequestHandler(new SettingToHttpHeader('WhereIs_Backend_Service_Token', 'AuthorizationX'));

        await configuration.slashCommands.provideSlashCommand(new WhereIsCommand(this));
    }

}
