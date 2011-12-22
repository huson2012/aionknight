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

package gameserver.skill.effect.modifier;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionModifiers")
public class ActionModifiers {

    @XmlElements({
        @XmlElement(name = "frontdamage", type = FrontDamageModifier.class),
        @XmlElement(name = "backdamage", type = BackDamageModifier.class),
        @XmlElement(name = "flyingdamage", type = FlyingDamageModifier.class),
        @XmlElement(name = "nonflyingdamage", type = NonFlyingDamageModifier.class),
        @XmlElement(name = "targetrace", type = TargetRaceDamageModifier.class),
        @XmlElement(name = "abnormaldamage", type = AbnormalDamageModifier.class)
    })
    protected List<ActionModifier> actionModifiers;
    /**
     * Gets the value of the actionModifiers property.
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StumbleDamageModifier }
     * {@link FrontDamageModifier }
     * {@link BackDamageModifier }
     * {@link StunDamageModifier }
     * {@link PoisonDamageModifier }
     * {@link TargetRaceDamageModifier }
     * 
     */
    public List<ActionModifier> getActionModifiers() {
        if (actionModifiers == null) {
            actionModifiers = new ArrayList<ActionModifier>();
        }
        return this.actionModifiers;
    }
}
