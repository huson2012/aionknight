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

package gameserver.model.gameobjects.player;

public class Emotion
{
	private int emotionId;
	private long emotion_date = 0;
	private long emotion_expires_time = 0;

	public Emotion(int emotionId, long emotion_date, long emotion_expires_time)
	{
		this.emotionId = emotionId;
		this.emotion_date = emotion_date;
		this.emotion_expires_time = emotion_expires_time;
	}

	public int getEmotionId()
	{
		return emotionId;
	}

	public long getEmotionDate()
	{
		return emotion_date;
	}

	public long getEmotionExpiresTime()
	{
		return emotion_expires_time;
	}

	public void setEmotionId(int emotionId)
	{
		this.emotionId = emotionId;
	}

	public long getEmotionTimeLeft()
	{
		if(emotion_expires_time == 0)
			return 0;

		long timeLeft = (emotion_date + ((emotion_expires_time )  * 1000L)) - System.currentTimeMillis();
		if(timeLeft < 0)
			timeLeft = 0;

		return timeLeft /1000L ;
	}

	public void setEmotionDate(long emotion_date)
	{
		this.emotion_date = emotion_date;
	}

	public void setEmotionExpiresTime(long emotion_expires_time)
	{
		this.emotion_expires_time = emotion_expires_time;
	}
}
