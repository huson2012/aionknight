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

package gameserver.network.rdc.commands.impl;

import gameserver.model.siege.Influence;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.rdc.commands.RDCCommand;
import gameserver.services.SiegeService;

public class AbyssStatus extends RDCCommand
{
	
	public AbyssStatus()
	{
		super("AbyssStatus");
	}
	
	@Override
	public String handleRequest(String... args)
	{
		String xml = "<AbyssStatus time=\"" + String.valueOf(System.currentTimeMillis() / 1000) + "\">";
		
			xml += "<InfluenceRatio Elyos=\"" + String.valueOf(Influence.getInstance().getElyos() * 100);
			xml += "\" Asmodians=\"" + String.valueOf(Influence.getInstance().getAsmos() * 100);
			xml += "\" Balaur=\"" + String.valueOf(Influence.getInstance().getBalaur() * 100);
			xml += "\" />";
			
			xml += "<AbyssTimer Remains=\"" + String.valueOf(SiegeService.getInstance().getSiegeTime()) + "\" />";
			
			xml += "<Locations>";
			
				for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
				{
					if(loc.getSiegeType() == SiegeType.ARTIFACT || loc.getSiegeType() == SiegeType.FORTRESS)
					{
						xml += "<SiegeLocation id=\"" + String.valueOf(loc.getLocationId()) + "\" type=\"" + loc.getSiegeType().name() + "\">";
						
							xml += "<HoldingRace>" + loc.getRace().name() + "</HoldingRace>";
							xml += "<HoldingLegion>" + String.valueOf(loc.getLegionId()) + "</HoldingLegion>";
							if(loc.getSiegeType() == SiegeType.FORTRESS)
							{
								xml += "<CurrentStatus>";
									xml += (loc.isVulnerable()) ? "VULNERABLE" : "INVULNERABLE";
								xml += "</CurrentStatus>";
								xml += "<NextStatus>";
									xml += (loc.getNextState() == 0) ? "INVULNERABLE" : "VULNERABLE";
								xml += "</NextStatus>";
							}
						
						xml += "</SiegeLocation>";
					}
				}
			
			xml += "</Locations>";
		
		xml += "</AbyssStatus>";
		return xml;
	}
}