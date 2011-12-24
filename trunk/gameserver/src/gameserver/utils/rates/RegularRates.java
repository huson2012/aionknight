/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.utils.rates;

import gameserver.configs.main.RateConfig;

public class RegularRates extends Rates
{
	@Override
	public int getGroupXpRate()
	{
		return RateConfig.GROUPXP_RATE;
	}

	@Override
	public int getDropRate()
	{
		return RateConfig.DROP_RATE;
	}

	@Override
	public int getChestDropRate()
	{
		return RateConfig.CHEST_DROP_RATE;
	}

	@Override
	public float getApNpcRate()
	{
		return RateConfig.AP_NPC_RATE;
	}

	@Override
	public float getApPlayerRate()
	{
		return RateConfig.AP_PLAYER_RATE;
	}

	@Override
	public int getQuestKinahRate()
	{
		return RateConfig.QUEST_KINAH_RATE;
	}

	@Override
	public int getQuestXpRate()
	{
		return RateConfig.QUEST_XP_RATE;
	}

	@Override
	public int getXpRate()
	{
		return RateConfig.XP_RATE;
	}

	@Override
	public float getCraftingXPRate()
	{
		return RateConfig.CRAFTING_XP_RATE;
	}

	@Override
	public float getCraftingLvlRate()
	{
		return RateConfig.CRAFTING_LVL_RATE;
	}

	@Override
	public float getGatheringXPRate()
	{
		return RateConfig.GATHERING_XP_RATE;
	}

	@Override
	public float getGatheringLvlRate()
	{
		return RateConfig.GATHERING_LVL_RATE;
	}

	@Override
	public int getKinahRate()
	{
		return RateConfig.KINAH_RATE;
	}

	@Override
	public int getBokerRate()
	{
		return RateConfig.BOKER_RATE;
	}
}
