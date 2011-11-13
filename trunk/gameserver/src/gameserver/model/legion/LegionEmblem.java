/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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