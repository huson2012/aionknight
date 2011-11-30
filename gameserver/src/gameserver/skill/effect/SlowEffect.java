package gameserver.skill.effect;

import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**

 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SlowEffect")
public class SlowEffect extends BufEffect
{

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, StatEnum.SLOW_RESISTANCE, null);
	}

	@Override
	public void startEffect(Effect effect)
	{
		super.startEffect(effect);
		effect.setAbnormal(EffectId.SLOW.getEffectId());
		effect.getEffected().getEffectController().setAbnormal(EffectId.SLOW.getEffectId());
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.SLOW.getEffectId());
	}
}
