package no.prokom.jira.plugins.jql;


import com.atlassian.jira.JiraDataType;
import com.atlassian.jira.JiraDataTypes;
import com.atlassian.jira.jql.operand.QueryLiteral;
import com.atlassian.jira.jql.query.QueryCreationContext;
import com.atlassian.jira.plugin.jql.function.AbstractJqlFunction;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.util.MessageSet;
import com.atlassian.jira.util.NotNull;
import com.atlassian.query.clause.TerminalClause;
import com.atlassian.query.operand.FunctionOperand;
import com.google.common.collect.Ordering;
import com.opensymphony.user.User;


import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ig57
 * Date: 04.01.12
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class firstUnreleasedVersionFunction extends AbstractJqlFunction {

    private final VersionManager versionManager;
    private final ProjectManager projectManager;
    

    public firstUnreleasedVersionFunction(VersionManager versionManager, ProjectManager projectManager)
    {
        this.versionManager = versionManager;
        this.projectManager = projectManager;
    }

    @Override
    public MessageSet validate(User user, @NotNull FunctionOperand functionOperand, @NotNull TerminalClause terminalClause) {
        return validateNumberOfArgs(functionOperand, 0);
    }

    @Override
    public List<QueryLiteral> getValues(@NotNull QueryCreationContext queryCreationContext, @NotNull FunctionOperand functionOperand, @NotNull TerminalClause terminalClause) {

        final List<QueryLiteral> list = new LinkedList<QueryLiteral>();
        Collection<Project> projects = projectManager.getProjectObjects();
        
        
        for(final Project project : projects)
        {
            for(final Version version : project.getVersions())
            {
                if(!version.isArchived() && !version.isReleased())
                {
                    list.add(new QueryLiteral(functionOperand, version.getId()));
                    break;
                }
            }
        }
        return list;
    }
    
    

    @Override
    public int getMinimumNumberOfExpectedArguments() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JiraDataType getDataType() {
        return JiraDataTypes.VERSION;
    }
}
