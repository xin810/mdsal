/*
 * Copyright (c) 2017 Pantheon Technologies s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.binding.javav2.dom.adapter.test;

import com.google.common.annotations.Beta;
import org.opendaylight.mdsal.dom.api.DOMSchemaService;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.util.ListenerRegistry;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaContextListener;
import org.opendaylight.yangtools.yang.model.api.SchemaContextProvider;

@Beta
public final class MockSchemaService implements DOMSchemaService, SchemaContextProvider {

    private SchemaContext schemaContext;

    ListenerRegistry<SchemaContextListener> listeners = ListenerRegistry.create();

    @Override
    public synchronized SchemaContext getGlobalContext() {
        return schemaContext;
    }

    @Override
    public synchronized SchemaContext getSessionContext() {
        return schemaContext;
    }

    @Override
    public ListenerRegistration<SchemaContextListener>
            registerSchemaContextListener(final SchemaContextListener listener) {
        return listeners.register(listener);
    }

    @Override
    public synchronized SchemaContext getSchemaContext() {
        return schemaContext;
    }

    public synchronized void changeSchema(final SchemaContext newContext) {
        schemaContext = newContext;
        for (final ListenerRegistration<SchemaContextListener> listener : listeners) {
            listener.getInstance().onGlobalContextUpdated(schemaContext);
        }
    }
}