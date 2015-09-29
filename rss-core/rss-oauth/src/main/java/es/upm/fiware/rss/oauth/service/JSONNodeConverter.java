/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.fiware.rss.oauth.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.pac4j.core.profile.converter.AttributeConverter;

/**
 *
 * @author francisco
 */
public class JSONNodeConverter implements AttributeConverter<JsonNode> {

    @Override
    public JsonNode convert(Object attribute) {
        if (attribute != null && attribute instanceof JsonNode) {
            return (JsonNode) attribute;
        }
        return null;
    }
    
}
