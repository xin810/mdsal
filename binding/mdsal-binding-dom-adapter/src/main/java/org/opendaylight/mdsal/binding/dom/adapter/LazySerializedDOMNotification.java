/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.binding.dom.adapter;

import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.binding.spec.reflect.BindingReflections;
import org.opendaylight.mdsal.dom.api.DOMNotification;
import org.opendaylight.yangtools.yang.binding.Notification;
import org.opendaylight.yangtools.yang.data.api.schema.ContainerNode;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;

/**
 * Lazy serialized implementation of DOM Notification.
 *
 * <p>
 * This implementation performs serialization of data, only if receiver
 * of notification actually accessed data from notification.
 *
 */
public final class LazySerializedDOMNotification implements DOMNotification {

    private final BindingNormalizedNodeSerializer codec;
    private final Notification data;
    private final SchemaPath type;

    private ContainerNode domBody;

    private LazySerializedDOMNotification(final BindingNormalizedNodeSerializer codec,
            final Notification data, final SchemaPath type) {
        this.codec = codec;
        this.data = data;
        this.type = type;
    }

    static DOMNotification create(final BindingNormalizedNodeSerializer codec, final Notification data) {
        final SchemaPath type = SchemaPath.create(true, BindingReflections.findQName(data.getImplementedInterface()));
        return new LazySerializedDOMNotification(codec, data, type);
    }

    @Override
    public SchemaPath getType() {
        return type;
    }

    @Override
    public ContainerNode getBody() {
        if (domBody == null) {
            domBody = codec.toNormalizedNodeNotification(data);
        }
        return domBody;
    }

    public Notification getBindingData() {
        return data;
    }
}
