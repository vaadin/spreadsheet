/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.poi.ss.usermodel.ClientAnchor;

import com.vaadin.addon.spreadsheet.client.OverlayInfo;
import com.vaadin.addon.spreadsheet.client.OverlayInfo.Type;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

/**
 * SheetImageWrapper is an utility class of the Spreadsheet component. In
 * addition to the image resource, this wrapper contains the images visibility
 * state, position and size.
 *
 * @author Vaadin Ltd.
 */
@SuppressWarnings("serial")
public class SheetImageWrapper extends SheetOverlayWrapper
        implements Serializable {

    private StreamResource resource;

    private final byte[] data;
    private final String MIMEType;

    public SheetImageWrapper(ClientAnchor anchor, String MIMEType,
            byte[] data) {
        super(anchor);
        this.MIMEType = MIMEType;
        this.data = data;
    }

    /**
     * Gets the resource containing this image
     *
     * @return Image resource
     */
    @Override
    public Resource getResource() {
        if (resource == null) {
            StreamSource streamSource = new StreamSource() {
                @Override
                public InputStream getStream() {
                    return new ByteArrayInputStream(data);
                }
            };
            resource = new StreamResource(streamSource, getId());
            resource.setMIMEType(MIMEType);
        }

        return resource;
    }

    @Override
    public Type getType() {
        return OverlayInfo.Type.IMAGE;
    }
}
