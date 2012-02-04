/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package languages;

import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.Language;

public class Russian extends Language
{
	public Russian()
	{
		super("ru");
		addSupportedLanguage("ru_RU");
		
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Версия сервера : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Добро пожаловать на сервер %s.\nCopyright (c) Aion-Knight Dev. Team\nРейты сервера :\nОпыт : %d\nКвесты : %d\nДроп : %d");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Добро пожаловать на сервер %s.\nCopyright (c) Aion-Knight Dev. Team\nРейты сервера :\nОпыт : %d\nКвесты : %d\nДроп : %d");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Добро пожаловать на сервер %s.\nРазработано командой Aion-Knight Dev.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "У вас нет прав, чтобы использовать эту команду");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Такого игрока нет в сейчас игре");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Параметр должен содержать цифры");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Параметры должны быть цифрами");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Произошла ошибка");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Команда неактивна");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Синтекс: //add <имя игрока> <id предмета> [<количество>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Предмет(ы) успешно добавлен игроку %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "Администратор %s дал вам %d предмет(ы)");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "Предмет %d не существует и/или не может быть добавлен %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Синтекс: //adddrop <id моба> <id вещи> <min> <max> <шанс>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Синтекс: //addset <имя игрока> <id сета>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "Сет %d не существует");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "Не хватает %d слотов в инвентаре, чтобы добавить этот сет");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "Предмет %d не может быть добавлен к %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "Сет %d успешно добавлен %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s выдал вам сет");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Синтекс: //addskill <id скилла> <уровен скилла");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Скилл %d успешно добавлен %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s выдал вам скилл");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Синтекс: //addtitle <id титула> <имя игрока> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Титул должен быть от 1 до 50");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Вы не можете выдать титул %d себе");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Невозможно применить титул %d к %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Вы успешно добавили титул %d себе");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "Вы успешно выдали игроку %s титул %d");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s выдал вам титул %d");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Синтекс: //send <имя файла>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "%s не найдено");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Пакет не послан!");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Канал %s закрыт, используйте канал %s или %s в зависимости от вашей расы");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "Все каналы деактивированы");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "Ваш канал успешно установлен %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Установлен канал %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "Вы не можете использовать этот канал!");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Доступные каналы: %s и %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Впишите %s чтобы покинуть канал");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "Вы не подписаны на канал");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Работа чата в данном канале не возможна! Используйте - %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "Вы покинули канал %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "Вы покинули %s и %s каналы");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "Вы можете опять присоединиться к каналам");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Игрок %s снова может присоединяться к каналам");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_ASMOS, "Асмодиане");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_ELYOS, "Элийцы");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_WORLD, "Мир");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_BOTH, "Асмодиане/Элийцы");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_ASMOS, "asmo");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_ELYOS, "ely");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_WORLD, "world");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_BOTH, "both");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "Вы не можете использовать канал, так как %s забанил вас по причине: %s, до разблокировки осталось: %s");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d умения %d стигма даны вам");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "Вам стало доступно %d умение.");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "Такой игровой команды не существует!");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Начисление XP отключено. Введите .xpon чтобы разблокировать");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Начисление XP отключено");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Начисление XP включено");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Начисление XP уже включено");
	}
}