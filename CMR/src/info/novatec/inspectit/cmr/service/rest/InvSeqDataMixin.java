package info.novatec.inspectit.cmr.service.rest;

import info.novatec.inspectit.communication.data.InvocationSequenceData;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class InvSeqDataMixin {
	@JsonIgnore abstract List<InvocationSequenceData> getClonedInvocationSequence();
}
