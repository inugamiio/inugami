/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.framework.configuration.services.mapping.transformers;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.ObjectTransformer;
import flexjson.transformer.Transformer;

/**
 * AbstractTransformerHelper
 *
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public abstract class AbstractTransformerHelper<T> extends AbstractTransformer implements Transformer {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {

        if (object != null) {
            getContext().writeOpenObject();
            process((T) object);
            getContext().writeCloseObject();
        }
    }

    // =========================================================================
    // abstract
    // =========================================================================
    public abstract void process(T object);

    // =========================================================================
    // TOOLS
    // =========================================================================
    public void field(final String name, final FieldTransformer function) {
        getContext().writeName(name);
        function.process();
        getContext().writeComma();
    }

    public <V> void fieldList(final String name, final Optional<List<V>> values) {
        fieldList(name, values, null);
    }

    public <V> void fieldList(final String name, final Optional<List<V>> values, final Consumer<V> function) {
        final Transformer objTransformer = new ObjectTransformer();

        getContext().writeName(name);
        getContext().writeOpenObject();
        getContext().writeName("present");
        getContext().write(String.valueOf(values.isPresent()));
        getContext().writeComma();

        getContext().writeName("data");
        getContext().writeOpenArray();
        if (values.isPresent()) {
            values.get().forEach(item -> {
                if (function == null) {
                    objTransformer.transform(item);
                } else {
                    function.accept(item);
                }
                getContext().writeComma();
            });
        }
        getContext().writeCloseArray();
        getContext().writeCloseObject();
        getContext().writeComma();
    }

    public <V> void fieldList(final String name, final List<V> values) {
        fieldList(name, values, null);
    }

    public <V> void fieldList(final String name, final List<V> values, final Consumer<V> function) {
        final Transformer objTransformer = new ObjectTransformer();
        getContext().writeName(name);
        getContext().writeOpenArray();
        if (values != null) {
            values.forEach(item -> {
                if (function == null) {
                    objTransformer.transform(item);
                } else {
                    function.accept(item);
                }
                getContext().writeComma();
            });
        }
        getContext().writeCloseArray();
        getContext().writeComma();
    }

    public void fieldBoolean(final String name, final FieldBooleanTransformer function) {
        getContext().writeName(name);
        getContext().write(String.valueOf(function.process()));
        getContext().writeComma();
    }

    public void fieldNull(final String name) {
        getContext().writeName(name);
        getContext().write("null");
        getContext().writeComma();
    }

    public void fieldString(final String name, final FieldStringTransformer function) {
        getContext().writeName(name);
        final String value = function.process();
        if (value == null) {
            getContext().write("null");
        } else {
            getContext().writeQuoted(function.process());
        }
        getContext().writeComma();
    }

    // =========================================================================
    // Functions
    // =========================================================================
    @FunctionalInterface
    protected interface FieldTransformer {
        void process();
    }

    @FunctionalInterface
    protected interface FieldBooleanTransformer {
        boolean process();
    }

    @FunctionalInterface
    protected interface FieldStringTransformer {
        String process();
    }

}
