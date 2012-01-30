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
 
package gameserver.model.legion;

import gameserver.model.gameobjects.PersistentState;

public class LegionEmblem
{
	private int	emblemVer = 0x00;
	private int	color_r = 0x00;
	private int	color_g = 0x00;
	private int	color_b = 0x00;
	private boolean	defaultEmblem = true;
	private boolean	isCustom = false;
	private PersistentState	persistentState;
	private boolean isUploading = false;
	private int uploadSize = 0;
	private int uploadedSize = 0;
	private byte[] uploadData;
	private byte[] customEmblemData;
	public byte[] getCustomEmblemData()
	{
		return customEmblemData;
	}

	public void setCustomEmblemData(byte[] customEmblemData)
	{
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		this.customEmblemData = customEmblemData;
	}

	public LegionEmblem()
	{
		setPersistentState(PersistentState.NEW);
	}

	public void setEmblem(int emblemVer, int color_r, int color_g, int color_b, boolean isCustom)
	{
		this.emblemVer = emblemVer;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		this.defaultEmblem = false;
		this.isCustom = isCustom;
	}

	public void setEmblem(int emblemVer, int color_r, int color_g, int color_b, boolean isCustom, byte[] customEmblemData)
	{
		this.emblemVer = emblemVer;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		this.defaultEmblem = false;
		this.isCustom = isCustom;
		this.customEmblemData = customEmblemData;
	}

	public int getEmblemVer()
	{
		return emblemVer;
	}

	public boolean getIsCustom()
	{
		return isCustom;
	}
	
	public void setIsCustom(boolean isCustom)
	{
		this.isCustom = isCustom;
	}

	public int getColor_r()
	{
		return color_r;
	}

	public int getColor_g()
	{
		return color_g;
	}

	public int getColor_b()
	{
		return color_b;
	}

	public boolean isDefaultEmblem()
	{
		return defaultEmblem;
	}

	public void setUploading(boolean isUploading)
	{
		this.isUploading = isUploading;
	}

	public boolean isUploading()
	{
		return isUploading;
	}

	public void setUploadSize(int emblemSize)
	{
		this.uploadSize = emblemSize;
	}

	public int getUploadSize()
	{
		return uploadSize;
	}

	public void addUploadData(byte[] data)
	{
		byte[] newData = new byte[uploadedSize];
		int i = 0;
		if(uploadData != null && uploadData.length > 0)
		{
			for(byte dataByte : uploadData)
			{
				newData[i] = dataByte;
				i++;
			}
		}
		for(byte dataByte : data)
		{
			newData[i] = dataByte;
			i++;
		}
		this.uploadData = newData;
	}

	public byte[] getUploadData()
	{
		return this.uploadData;
	}

	public void addUploadedSize(int uploadedSize)
	{
		this.uploadedSize += uploadedSize;
	}

	public int getUploadedSize()
	{
		return uploadedSize;
	}

	public void resetUploadSettings()
	{
		this.isUploading = false;
		this.uploadedSize = 0;
	}

	public void setPersistentState(PersistentState persistentState)
	{
		switch(persistentState)
		{
			case UPDATE_REQUIRED:
				if(this.persistentState == PersistentState.NEW)
					break;
			default:
				this.persistentState = persistentState;
		}
	}

	public PersistentState getPersistentState()
	{
		return persistentState;
	}
}
