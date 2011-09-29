package org.motechproject.ivr.kookoo.action.event;

import org.motechproject.eventtracking.service.EventService;
import org.motechproject.ivr.kookoo.action.BaseAction;
import org.motechproject.ivr.kookoo.eventlogging.CallEventConstants;
import org.motechproject.ivr.kookoo.service.KookooCallDetailRecordsService;
import org.motechproject.server.service.ivr.CallEvent;
import org.motechproject.server.service.ivr.IVRRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseEventAction extends BaseAction {

    @Autowired
    protected EventService eventService;

    @Autowired
    protected KookooCallDetailRecordsService kookooCallDetailRecordsService;

    public BaseEventAction() {
    }

    public BaseEventAction(KookooCallDetailRecordsService kookooCallDetailRecordsService) {
        this.kookooCallDetailRecordsService = kookooCallDetailRecordsService;
    }

    public String handle(String callId, IVRRequest ivrRequest, HttpServletRequest request, HttpServletResponse response) {
        String responseXML = createResponse(ivrRequest, request, response);
        publishCallEvent(callId, ivrRequest, responseXML);
        postHandle(callId, ivrRequest, request, response);
        return responseXML;
    }

    protected void publishCallEvent(String callId, IVRRequest ivrRequest, String responseXML) {
        Map<String, String> callEventData = callEventData(ivrRequest);
        callEventData.put(CallEventConstants.RESPONSE_XML, responseXML);
        CallEvent callEvent = new CallEvent(ivrRequest.callEvent().key(), callEventData);
        kookooCallDetailRecordsService.appendEvent(callId, callEvent);
    }

    protected Map<String, String> callEventData(IVRRequest ivrRequest) {
        return new HashMap<String, String>();
    }

    public void postHandle(String callId, IVRRequest ivrRequest, HttpServletRequest request, HttpServletResponse response) {
    }
}
