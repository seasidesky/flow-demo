/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.hummingbird.demo.addressbook.ui;

import java.util.List;
import java.util.function.Consumer;

import com.vaadin.annotations.Tag;
import com.vaadin.hummingbird.demo.addressbook.backend.Contact;
import com.vaadin.hummingbird.demo.addressbook.backend.ContactService;
import com.vaadin.hummingbird.html.HtmlComponent;
import com.vaadin.hummingbird.util.JsonUtils;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * @author Vaadin Ltd.
 */
@Tag("contacts-table")
class ContactsTable extends HtmlComponent {
    private final transient Consumer<String> selectedRowIdProcessor;

    ContactsTable(Consumer<String> selectedRowIdProcessor) {
        this.selectedRowIdProcessor = selectedRowIdProcessor;

        updateTableContents();

        getElement().addEventListener("row-selection-updated",
                e -> processEventData(e.getEventData()), "event.detail");
    }

    void updateTableContents() {
        List<Contact> allContacts = ContactService.getDemoService().findAll("");

        JsonArray contacts = JsonUtils.createArray();
        for (int i = 0; i < allContacts.size(); i++) {
            contacts.set(i, allContacts.get(i).toJsonObject());
        }
        getElement().setPropertyJson("contacts", contacts);
    }

    private void processEventData(JsonObject eventData) {
        JsonValue jsonValue = eventData.get("event.detail");
        final String selectedId;
        switch (jsonValue.getType()) {
        case STRING:
            selectedId = jsonValue.asString();
            break;
        case NULL:
            selectedId = null;
            break;
        default:
            throw new IllegalStateException(
                    "Unexpected json value: " + jsonValue);
        }
        selectedRowIdProcessor.accept(selectedId);
    }
}