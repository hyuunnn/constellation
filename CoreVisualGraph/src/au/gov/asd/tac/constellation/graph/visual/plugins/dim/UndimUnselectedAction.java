/*
 * Copyright 2010-2024 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.graph.visual.plugins.dim;

import au.gov.asd.tac.constellation.graph.node.GraphNode;
import au.gov.asd.tac.constellation.graph.node.plugins.SimplePluginAction;
import au.gov.asd.tac.constellation.graph.visual.VisualGraphPluginRegistry;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * un-dim all unselected all elements
 *
 * @author algol
 */
@ActionID(category = "Selection", id = "au.gov.asd.tac.constellation.functionality.dim.UndimUnselectedAction")
@ActionRegistration(displayName = "#CTL_UndimUnselectedAction", iconBase = "au/gov/asd/tac/constellation/graph/visual/plugins/dim/resources/undim_unselected.png", surviveFocusChange = true)
@ActionReference(path = "Menu/Display/Dimming", position = 2500)
@Messages("CTL_UndimUnselectedAction=Undim Unselected")
public final class UndimUnselectedAction extends SimplePluginAction {

    public UndimUnselectedAction(final GraphNode context) {
        super(context, VisualGraphPluginRegistry.UNDIM_UNSELECTED);
    }
}
