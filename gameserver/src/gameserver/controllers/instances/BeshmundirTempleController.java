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

package gameserver.controllers.instances;

import gameserver.ai.events.Event;
import gameserver.controllers.NpcController;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class BeshmundirTempleController extends NpcController
{
    // Private VisibleObject target = null;
    Npc npc = getOwner();
    
    @Override
    public void onDialogRequest(final Player player){
        getOwner().getAi().handleEvent(Event.TALK);
        
        NpcTemplate npctemplate = DataManager.NPC_DATA.getNpcTemplate(getOwner().getNpcId());
        if (npctemplate.getNameId() == 354971)
        {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
            return;
        }

        switch (getOwner().getNpcId()){
            case 730275: {
                ThreadPoolManager.getInstance().schedule(new Runnable(){
                    @Override
                    public void run(){
                        final int defaultUseTime = 3000;
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),getOwner().getObjectId(), defaultUseTime, 1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable(){
                            @Override
                            public void run(){
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(), defaultUseTime, 0));
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 443));
                                ThreadPoolManager.getInstance().schedule(new Runnable(){
                                    @Override
                                    public void run(){
                                        TeleportService.teleportTo(player, 300170000, 528.27496f, 1345.001f, 223.52919f, 14);
                                    }
                                }, 35000);
                            }
                        }, defaultUseTime);
                    }
                }, 0);
            }
        }
    }
    
    @Override
    public void onDialogSelect(int dialogId, final Player player, int questId)
    {
        Npc npc = getOwner();

        if (dialogId == 10000 && (npc.getNpcId() == 799517)) // Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 448));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
                @Override
                        public void run(){
                            TeleportService.teleportTo(player, 300170000, 958.45233f, 430.4892f, 219.80301f, 0);
                        }
            }, 23000);
        }
        else if (dialogId == 10000 && (npc.getNpcId() == 799518)) // Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 449));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
                    public void run(){
                        TeleportService.teleportTo(player, 300170000, 822.0199f, 465.1819f, 220.29918f, 0);
                    }
        }, 23000);
        }
        else if (dialogId == 10000 && (npc.getNpcId() == 799519)) // Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 450));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
                    public void run(){
                        TeleportService.teleportTo(player, 300170000, 777.1054f, 300.39005f, 219.89926f, 94);
                    }
        }, 23000);
        }
        else if (dialogId == 10000 && (npc.getNpcId() == 799520)) // Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 451));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
                    public void run(){
                        TeleportService.teleportTo(player, 300170000, 942.3092f, 270.91855f, 219.86185f, 86);
                    }
        }, 23000);
        }
    }
}