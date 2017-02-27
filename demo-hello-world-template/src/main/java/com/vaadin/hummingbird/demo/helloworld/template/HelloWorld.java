/*
 * Copyright 2000-2016 Vaadin Ltd.
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
package com.vaadin.hummingbird.demo.helloworld.template;

import com.vaadin.annotations.EventHandler;
import com.vaadin.hummingbird.template.model.TemplateModel;
import com.vaadin.ui.AngularTemplate;

/**
 * The one and only view in the hello world application.
 */
public class HelloWorld extends AngularTemplate {
    /**
     * Model for the template.
     */
    public interface HelloWorldModel extends TemplateModel {
        /**
         * Sets the text to show in the template.
         *
         * @param text
         *            the text to show in the template
         */
        void setText(String text);
    }

    @Override
    protected HelloWorldModel getModel() {
        return (HelloWorldModel) super.getModel();
    }

    @EventHandler
    private void sayHello(String inputValue) {
        // Called from the template click handler
        getModel().setText(inputValue);
    }
}
