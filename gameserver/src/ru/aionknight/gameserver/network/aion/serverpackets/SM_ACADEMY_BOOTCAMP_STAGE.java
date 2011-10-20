package ru.aionknight.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import ru.aionknight.gameserver.network.aion.AionConnection;
import ru.aionknight.gameserver.network.aion.AionServerPacket;

/**
 * @author kosyachok
 *
 */
public class SM_ACADEMY_BOOTCAMP_STAGE extends AionServerPacket
{
    private int stage;
    private int round;
    private boolean win;
    
    public SM_ACADEMY_BOOTCAMP_STAGE(int stage, int round, boolean win)
    {
        this.stage = stage;
        this.round = round;
        this.win = win;
    }
    
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, 0); //unk
        writeD(buf, 0);
        
        int stagevalue = (stage * 1000) + (win ? 100 : 0) + round; // F@#$ING NCSoft!!! WHY!?
        writeD(buf, stagevalue);
    }
}
