/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.services;

import gameserver.model.gameobjects.Monster;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.SpawnEngine;

public class BeshmundirInstanceService
{
    public void onGroupReward(Monster monster, PlayerGroup group)
    {
        if(monster.getObjectTemplate().getTemplateId() == 216238)
        {
            SpawnTemplate soulcaller, manadar, Macunbello, Plaguebearer, Vehala, Virhana, Dorakiki, Chopper, Fixit, Hakiki, Taros;
            soulcaller = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216159, 1356f, 388f, 249f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(soulcaller, group.getGroupLeader().getInstanceId());
            manadar = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216160, 1085f, 621f, 234f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(manadar, group.getGroupLeader().getInstanceId());
            Macunbello = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216735, 981f, 134f, 241f, (byte)31, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Macunbello, group.getGroupLeader().getInstanceId());
            Plaguebearer = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216163, 1052f, 83f, 233f, (byte)0, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Plaguebearer, group.getGroupLeader().getInstanceId());
            Vehala = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216162, 1101f, 34f, 234f, (byte)30, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Vehala, group.getGroupLeader().getInstanceId());
            Virhana = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216165, 1215f, 659f, 250f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Virhana, group.getGroupLeader().getInstanceId());
            Dorakiki = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216169, 1176.92f, 1216.06f, 282.93f, (byte)106, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Dorakiki, group.getGroupLeader().getInstanceId());
            Chopper = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 281649, 1174.67f, 1213.71f, 283.52f, (byte)106, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Chopper, group.getGroupLeader().getInstanceId());
            Fixit = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 281647, 1172.36f, 1211.63f, 283.5f, (byte)106, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Fixit, group.getGroupLeader().getInstanceId());
            Hakiki = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 281648, 1170.65f, 1210.18f, 283.61f, (byte)108, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Hakiki, group.getGroupLeader().getInstanceId());
            Taros = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216167, 1400.43f, 1284.99f, 302.75f, (byte)103, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Taros, group.getGroupLeader().getInstanceId());
        }
        if(monster.getObjectTemplate().getTemplateId() == 216587)
        {
            SpawnTemplate spawn;
            
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 799518, 933f, 444f, 220f, (byte)25, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216588) // Второй остров
        {
            SpawnTemplate spawn;
            
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 799519, 788f, 442f, 220f, (byte)25, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216589) // Третий остров
        {
            SpawnTemplate spawn;
            
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 799520, 817f, 276f, 220f, (byte)0, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        if(monster.getObjectTemplate().getTemplateId() == 216739) {
            
            group.setInstanceKills(group.getInstanceKills() + 1);
            
            if (group.getInstanceKills() == 15){
                SpawnTemplate Ahbana;
                Ahbana = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216158, 1356f, 96f, 247f, (byte)29, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(Ahbana, group.getGroupLeader().getInstanceId());
            }
        }
        if(monster.getObjectTemplate().getTemplateId() == 216159)
        {
            SpawnTemplate Shadowshift, Thurzon;
                        
            Shadowshift = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216166, 1394f, 458f, 243f, (byte)47, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Shadowshift, group.getGroupLeader().getInstanceId());

            Thurzon = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216525, 1398f, 532f, 241f, (byte)60, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Thurzon, group.getGroupLeader().getInstanceId());
        }
        
        if(monster.getObjectTemplate().getTemplateId() == 216170) // Привратник Darfall
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216175, 1434.53f, 1584.14f, 305.825f, (byte)98, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }        
        else if(monster.getObjectTemplate().getTemplateId() == 216175)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216171, 1394.33f, 1451.41f, 307.793f, (byte)119, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }        
        else if(monster.getObjectTemplate().getTemplateId() == 216171)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216177, 1501.43f, 1593.96f, 329.945f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216177)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216173, 1573.23f, 1426.84f, 304.66f, (byte)54, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216173)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216181, 1640.2f, 1428.82f, 305.835f, (byte)57, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216181)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216172, 1522.82f, 1353.76f, 307.793f, (byte)33, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216172)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216179, 1631.57f, 1495.91f, 329.945f, (byte)66, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
        else if(monster.getObjectTemplate().getTemplateId() == 216179)
        {
            SpawnTemplate spawn;
            spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 216182, 1603.43f, 1593.01f, 307.034f, (byte)77, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }

        if(monster.getObjectTemplate().getTemplateId() == 216182)
        {
            SpawnTemplate spawn;
			spawn = SpawnEngine.getInstance().addNewSpawn(300170000, group.getGroupLeader().getInstanceId(), 730275, 1611.1266f, 1604.6935f, 309.5f, (byte)0, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
        }
    }
    
    public static BeshmundirInstanceService getInstance()
    {
        return SingletonHolder.instance;
    }
    
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final BeshmundirInstanceService instance = new BeshmundirInstanceService();
    }   
}