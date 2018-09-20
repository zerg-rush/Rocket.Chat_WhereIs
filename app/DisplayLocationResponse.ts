import {IModify, IRead} from '@rocket.chat/apps-engine/definition/accessors';

import {SlashCommandContext} from '@rocket.chat/apps-engine/definition/slashcommands';
import {IMessageAttachment} from '@rocket.chat/apps-engine/definition/messages';

export async function displayLocationResponse(
    message: { text: string, attachments: Array<IMessageAttachment>, icon_emoji: string },
    context: SlashCommandContext, read: IRead, modify: IModify): Promise<void> {

    const botUsername = await read.getEnvironmentReader().getSettings().getValueById('WhereIs_Bot');
    const botUser = await read.getUserReader().getByUsername(botUsername);
    const backendUrl = await read.getEnvironmentReader().getSettings().getValueById('WhereIs_Backend_Service_URL')
    const botAvatarUrl = backendUrl.replace("/api/whereis", "/images/whereis.jpg");

    const builder = modify.getCreator().startMessage()
        .setSender(botUser || context.getSender())
        .setRoom(context.getRoom())
        .setUsernameAlias('Where Is')
        .setAvatarUrl(botAvatarUrl)
        .setText(message.text)
        .setAttachments(message.attachments);

    await modify.getNotifier().notifyUser(context.getSender(), builder.getMessage());
}
