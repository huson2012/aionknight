package gameserver.skill.effect;


import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvulnerableWingEffect")
public class InvulnerableWingEffect extends EffectTemplate
{
	@Override
	public void applyEffect(final Effect effect)
	{
		//NOTE look at LifeStatsRestoreService.java line 169
		effect.addToEffectedController();
	}
}

