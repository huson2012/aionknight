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

public class PremiumRates extends Rates
{
	@Override
	public int getGroupXpRate()
	{
		return RateConfig.PREMIUM_GROUPXP_RATE;
	}

	@Override
	public float getApNpcRate()
	{
		return RateConfig.PREMIUM_AP_NPC_RATE;
	}

	@Override
	public float getApPlayerRate()
	{
		return RateConfig.PREMIUM_AP_PLAYER_RATE;
	}

	@Override
	public int getDropRate()
	{
		return RateConfig.PREMIUM_DROP_RATE;
	}
	
	@Override
	public int getChestDropRate()
	{
		return RateConfig.PREMIUM_CHEST_DROP_RATE;
	}

	@Override
	public int getQuestKinahRate()
	{
		return RateConfig.PREMIUM_QUEST_KINAH_RATE;
	}

	@Override
	public int getQuestXpRate()
	{
		return RateConfig.PREMIUM_QUEST_XP_RATE;
	}

	@Override
	public int getXpRate()
	{
		return RateConfig.PREMIUM_XP_RATE;
	}

	@Override
	public float getCraftingXPRate()
	{
		return RateConfig.PREMIUM_CRAFTING_XP_RATE;
	}
	
	@Override
	public float getCraftingLvlRate()
	{
		return RateConfig.PREMIUM_CRAFTING_LVL_RATE;
	}

	@Override
	public float getGatheringXPRate()
	{
		return RateConfig.PREMIUM_GATHERING_XP_RATE;
	}
	
	@Override
	public float getGatheringLvlRate()
	{
		return RateConfig.PREMIUM_GATHERING_LVL_RATE;
	}

	@Override
	public int getKinahRate()
	{
		return RateConfig.PREMIUM_KINAH_RATE;
	}
}