package org.goodmath.pica.ast.quarks;

import lombok.Getter;
import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.TypeParamSpec;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.errors.ErrorLog;
import org.goodmath.pica.errors.PicaTypeError;
import org.goodmath.pica.types.*;
import org.goodmath.pica.util.Twist;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Flavors: The equivalent of an OO interface for quarks.
 *
 * <p> A flavor declares the set of publicly visible channels
 * for a quark.</p>
 */
@Getter
public class FlavorDef extends Definition {
    private final List<Type> composes;
    private final List<ChannelDef> channels;

    public FlavorDef(
            Identifier moduleName,
            String name,
            Optional<List<TypeParamSpec>> typeParams,
            List<Type> composes,
            List<ChannelDef> channels,
            Location loc) {
        super(moduleName, name, typeParams, loc);
        this.composes = composes;
        this.channels = channels;
    }

    @Override
    public Twist twist() {
        return Twist.obj("FlavorDef",
                Twist.attr("name", getName()),
                Twist.arr("typeParams", getTypeParams().orElse(Collections.emptyList())),
                Twist.arr("composes", getComposes()),
                Twist.arr("channels", getChannels()));
    }

    @Override
    public List<DefinedName> getDefinedNames() {
        return List.of(DefinedName.flavorDefinition(getName(), this));
    }


    @Override
    public boolean validate() {
        boolean foundError = false;
        if (!composes.isEmpty()) {
            for (Type t : composes) {
                Optional<Definition> cDef = new ConcreteType(getModule(), t).getTypeDefinition();
                if (cDef.isEmpty() || !(cDef.get() instanceof FlavorDef)) {
                    ErrorLog.logError(new PicaTypeError(
                            "The composed types of a Flavor must be flavor types, but found " + cDef));

                    foundError = true;
                }

            }
        }
        for (ChannelDef cd: getChannels()) {

        }
        return foundError;
    }
}
