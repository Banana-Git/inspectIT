package info.novatec.inspectit.cmr.service.rest;

import info.novatec.inspectit.cmr.model.PlatformIdent;
import info.novatec.inspectit.cmr.service.IGlobalDataAccessService;
import info.novatec.inspectit.cmr.service.IInvocationDataAccessService;
import info.novatec.inspectit.cmr.service.rest.error.JsonError;
import info.novatec.inspectit.cmr.util.AgentStatusDataProvider;
import info.novatec.inspectit.communication.comparator.ResultComparator;
import info.novatec.inspectit.communication.data.InvocationSequenceData;
import info.novatec.inspectit.communication.data.cmr.AgentStatusData;
import info.novatec.inspectit.exception.BusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Simon Becker
 *
 */
@Controller
@RequestMapping(value = "/agentdata")
public class AgentDataRestfulService {
	
	/**
	 * Reference to the existing {@link IGlobalDataAccessService}.
	 */
	@Autowired
	private IGlobalDataAccessService globalDataAccessService;

	/**
	 * Reference to the existing {@link IInvocationDataAccessService}.
	 */
	@Autowired
	private IInvocationDataAccessService invocationDataAccessService;
	
	
	
	
	/**
	 * Returns Overview of captured invocation sequences
	 * <p>
	 * 
	 * <i> Example URL: /agentdata/get-invocation-sequences?platformId=30</i>
	 * 
	 * @param platformId
	 * @return
	 * @throws BusinessException 
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "get-invocation-sequences")
	@ResponseBody
	public List<InvocationSequenceData> getInvocationSequenceOverview(@RequestParam(value = "platformId", required = true) long platformId) {
		return invocationDataAccessService.getInvocationSequenceOverview(platformId, 100, null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "get-invocation-sequences30")
	@ResponseBody
	public List<InvocationSequenceData> getInvocationSequenceOverviewFix() {
		return invocationDataAccessService.getInvocationSequenceOverview(30, 100, null);
	}
	
	
	
	/**
	 * {@link AgentStatusDataProvider}.
	 */
	@Autowired
	AgentStatusDataProvider agentStatusProvider;
	
	@RequestMapping(method = RequestMethod.GET, value = "overview")
	@ResponseBody
	public Map<PlatformIdent, AgentStatusData> getAgentsOverview() {		
		return globalDataAccessService.getAgentsOverview();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "myOverview")
	@ResponseBody
	// returns Map like this one: {"tomcat":"CONNECTED","jetty":"DISCONNECTED"}
	//public Map<String, AgentStatusData.AgentConnection> getMyAgentsOverview() {
	public Map<String, String> getMyAgentsOverview() {
		Map<PlatformIdent, AgentStatusData> agentsOverviewMap = getAgentsOverview();
		//Map<String, AgentStatusData.AgentConnection> agentsMap = new HashMap<String, AgentStatusData.AgentConnection>();
		Map<String, String> agentsMap = new HashMap<String, String>();
		
		for (PlatformIdent platformIdent : agentsOverviewMap.keySet()) {
			// agentsMap.put(platformIdent.getAgentName(), agentsOverviewMap.get(platformIdent).getAgentConnection());
			agentsMap.put(platformIdent.getAgentName(),
					agentsOverviewMap.get(platformIdent).getAgentConnection().toString() + ", id=" + platformIdent.getId() + ", Version=" + platformIdent.getVersion());
		}
		
		return agentsMap;
	}

	/**
	 * Returns complete Agent Data
	 * <p>
	 * 
	 * <i> Example URL: /agentdata/get-complete-agent?id=30</i>
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException 
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "get-complete-agent")
	@ResponseBody
	public PlatformIdent getCompleteAgent(@RequestParam(value = "id", required = true) long id) throws BusinessException {
		return globalDataAccessService.getCompleteAgent(id);
	}
	
	/**
	 * Handling of all the exceptions happening in this controller.
	 * 
	 * @param exception
	 *            Exception being thrown
	 * @return {@link ModelAndView}
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception exception) {
		return new JsonError(exception).asModelAndView();
	}
	
}
