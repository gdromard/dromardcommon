/**
 * 	File : AssemblyTreePerformanceTest.java 17 juin 08
 */
package net.dromard.common.httpbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tests case container.
 * <br>
 * @author Gabriel Dromard
 */
public final class OrkestraHttpScenariiTestsExample {
    /** The list of actions for a global REALIST test case. */
    public static final int ALL_PAGES_ACTIONS = 111;
    /** The list of actions for testing assembly tree grid generation. */
    public static final int ASSEMBLY_TREE_ACTIONS = 222;
    /** The list of actions for testing lead grid generation. */
    public static final int LEADS_GRID_ACTIONS = 333;
    /** The view of an assembly. */
    public static final int ASSEMBLY_VIEW_ACTIONS = 444;
    /** The view of a lead. */
    public static final int LEAD_VIEW_ACTIONS = 555;

    /** Application URL context. */
    //private static final String CONTEXT = "http://localhost:8080/orkestra/";
    private static final String CONTEXT = "http://localhost:8080/design2x/";
    //private static final String CONTEXT = "http://44.4.169.22:9080/orkestra/";
    /** Random object. */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /** Private constructor. */
    private OrkestraHttpScenariiTestsExample() {
    }

    /**
     * @param actions The action identifier of the list to be returned.
     * @return The wanted actions.
     */
    public static List getActions(final int actions) {
        switch (actions) {
            case ALL_PAGES_ACTIONS:     return getAllPagesActions();
            case LEADS_GRID_ACTIONS:    return getLeadsGridActions();
            case ASSEMBLY_TREE_ACTIONS: return getAssemblyTreeGridActions();
            case ASSEMBLY_VIEW_ACTIONS: return getAssemblyViewActions();
            case LEAD_VIEW_ACTIONS:     return getLeadViewActions();
            default: return null;
        }
    }
    /**
     * @return The list of actions for testing assembly view.
     */
    private static List getLoginActions() {
        List actions = new ArrayList();
        actions.add(new HttpRequestAction(CONTEXT, "checkout.action", false));
        actions.add(new HttpRequestAction(CONTEXT, "j_security_check?j_username=f.williamson&j_password=*****", false));
        //actions.add(new HttpRequestAction(CONTEXT, "j_security_check?j_username=d.moore&j_password=*****", false));
        return actions;
    }

    // These URLs are valid for 900 assemblies FLAT test
    /**
     * @return The list of actions for a global REALIST test case.
     */
    private static List getAllPagesActions() {
        List actions = getLoginActions();
        //ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "timewatch?reset=true"));
        // Reset Reporting Context
        //ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "reportingContext.action?method=edit"));
        //ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "reportingContext.action?method=save&reportingContext.modelUuid=all.models.id&reportingContext.baselineUuid=CURRENT&reportingContext.productUnitUuid=UNIT_01"));
        // Set Reporting Context with model 601
        //ACTIONS.add(new Action("reportingContext.action?method=save&reportingContext.modelUuid=MODEL_001&reportingContext.baselineUuid=CURRENT&reportingContext.productUnitUuid=UNIT_01"));
        // Home Page
        actions.add(new HttpRequestAction(CONTEXT, "checkout.action?method=display"));
        actions.add(new HttpRequestAction(CONTEXT, "assemblyCheckoutGridDefinition.action?Skipit=true"));
        actions.add(new HttpRequestAction(CONTEXT, "assemblyCheckoutGridData.action?Model=0"));
        actions.add(new HttpRequestAction(CONTEXT, "leadCheckoutGridDefinition.action?Skipit=true"));
        actions.add(new HttpRequestAction(CONTEXT, "leadCheckoutGridData.action"));
        // Assembly Page
        actions.add(new HttpRequestAction(CONTEXT, "ps.action"));
        actions.add(new HttpRequestAction(CONTEXT, "assemblyTreeGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "modelLeadGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "costLineGridDefinition.action?isLead=true"));
        actions.add(new HttpRequestAction(CONTEXT, "assemblyTreeGridData.action?Model=0"));
        actions.add(new HttpRequestAction(CONTEXT, "modelLeadGridData.action?Model=0"));
        actions.add(new HttpRequestAction(CONTEXT, "costLineGridData.action?Conversionrequired=true&Model=0"));
        // Assembly View Page
        actions.add(new HttpRequestAction(CONTEXT, "assembly.action?method=view&uuid=ASSEMBLY_105_V1"));
        actions.add(new HttpRequestAction(CONTEXT, "attachmentGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "attachmentGridData.action?name=assemblyEditForm&property=assemblyBean.attachments"));
        actions.add(new HttpRequestAction(CONTEXT, "assemblyCostLineGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "assemblyCostLineGridData.action?Conversionrequired=true&Effectivityuuid=EFF_105_MODEL_002"));
        // Lead Page
        actions.add(new HttpRequestAction(CONTEXT, "lead.action"));
        actions.add(new HttpRequestAction(CONTEXT, "leadGridData.action?Model=0"));
        actions.add(new HttpRequestAction(CONTEXT, "leadGridDefinition.action?CanSelect=1"));
        actions.add(new HttpRequestAction(CONTEXT, "modelAssemblyGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "costLineGridDefinition.action?isLead=true"));
        actions.add(new HttpRequestAction(CONTEXT, "modelAssemblyGridData.action?Leaduuid=&Model=0"));
        actions.add(new HttpRequestAction(CONTEXT, "costLineGridData.action?Conversionrequired=true&Effectivityuuid=&Leaduuid=&Type=Lead&Model=0"));
        // Lead View Page
        actions.add(new HttpRequestAction(CONTEXT, "gridContextualMenu.action?uuid=ITEM_2_V1&type=lead&gridId=leads&imported=false&reserved=false&workingCopy=false"));
        actions.add(new HttpRequestAction(CONTEXT, "lead.action?method=viewIteration&uuid=ITEM_2_V1"));
        actions.add(new HttpRequestAction(CONTEXT, "leadCostLineGridDefinition.action?isLead=true"));
        actions.add(new HttpRequestAction(CONTEXT, "leadCostLineGridData.action?domain=1101&Conversionrequired=true&Tabid=0&Effectivityuuid=EFF_10_MODEL_001"));
        actions.add(new HttpRequestAction(CONTEXT, "leadCostLineGridDefinition.action?isLead=true"));
        actions.add(new HttpRequestAction(CONTEXT, "leadCostLineGridData.action?domain=1101&Conversionrequired=true&Tabid=1&Effectivityuuid=EFF_10_MODEL_002"));
        actions.add(new HttpRequestAction(CONTEXT, "attachmentGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "attachmentGridData.action?name=leadEditForm&property=leadBean.attachments"));
        // Lead Reserve Page
        /* Can not be done with many concurrent thread
        ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "modelAssemblyGridData.action?Leaduuid=ITEM_4_V1&Model=0"));
        ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "modelAssemblyGridData.action?Leaduuid=ITEM_10_V1&Model=0"));
        ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "gridContextualMenu.action?uuid=ITEM_10_V1&type=lead&gridId=leads&imported=false&reserved=false&workingCopy=false"));
        ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "lead.action?method=checkout&uuid=ITEM_10"));
        ALL_PAGES_ACTIONS.add(new HttpRequestAction(context, "lead.action?method=deleteWorkingCopy&uuid=ITEM_10_V1"));
         */

        // Admin Pages
        actions.add(new HttpRequestAction(CONTEXT, "admin.users.action"));
        actions.add(new HttpRequestAction(CONTEXT, "usersGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "usersGridData.action"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.user.action?method=edit&uuid=USER_02"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.user.action?method=save"));

        actions.add(new HttpRequestAction(CONTEXT, "admin.scopes.action"));
        actions.add(new HttpRequestAction(CONTEXT, "scopesGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "scopesGridData.action"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.scope.action?method=edit&uuid=SCOPE_002"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.scope.action?method=save&custom.scope.prefix['0'].uuid=PREFIX_004&custom.scope.prefix['0'].code=PRF_TEST"));

        actions.add(new HttpRequestAction(CONTEXT, "admin.usergroups.action"));
        actions.add(new HttpRequestAction(CONTEXT, "userGroupsGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "userGroupsGridData.action"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.usergroup.action?method=edit&uuid=G1"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.usergroup.action?method=save"));

        actions.add(new HttpRequestAction(CONTEXT, "admin.contexts.action"));
        actions.add(new HttpRequestAction(CONTEXT, "contextGroupGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "contextGroupGridData.action"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.context.action?method=edit&uuid=CTX_GROUP_01"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.context.action?method=save"));

        actions.add(new HttpRequestAction(CONTEXT, "admin.studyAxis.action"));
        actions.add(new HttpRequestAction(CONTEXT, "studyAxisGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "studyAxisGridData.action"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.studyAxis.action?method=edit&uuid=DECISION_DRIVER_2"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.studyAxis.action?method=save"));

        // Parameters
        actions.add(new HttpRequestAction(CONTEXT, "admin.baselines.action"));
        actions.add(new HttpRequestAction(CONTEXT, "baselinesGridDefinition.action"));
        actions.add(new HttpRequestAction(CONTEXT, "baselinesGridData.action"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.baseline.action?method=add"));
        actions.add(new HttpRequestAction(CONTEXT, "admin.baseline.action?method=save&baseline.name=TEST" + RANDOM.nextLong()));
        return actions;
    }
    /**
     * @return The list of actions for testing assembly view.
     */
    private static List getAssemblyViewActions() {
        List actions = getLoginActions();
        actions.add(new HttpRequestAction(CONTEXT, "assembly.action?method=view&uuid=ASSEMBLY_105_V1"));
        return actions;
    }
    /**
     * @return The list of actions for testing lead view.
     */
    private static List getLeadViewActions() {
        List actions = getLoginActions();
        actions.add(new HttpRequestAction(CONTEXT, "lead.action?method=viewIteration&uuid=ITEM_2_V1"));
        return actions;
    }
    /**
     * @return The list of actions for testing assembly tree grid generation.
     */
    private static List getAssemblyTreeGridActions() {
        // Assembly Tree Grid
        List actions = getLoginActions();
        actions.add(new HttpRequestAction(CONTEXT, "assemblyTreeGridData.action?Model=0"));
        return actions;
    }
    /**
     * @return The list of actions for testing lead grid generation.
     */
    private static List getLeadsGridActions() {
        // Leads Grid
        List actions = getLoginActions();
        actions.add(new HttpRequestAction(CONTEXT, "lead.action"));
        actions.add(new HttpRequestAction(CONTEXT, "leadGridData.action"));
        return actions;
    }
}
