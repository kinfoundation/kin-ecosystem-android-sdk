package com.kin.ecosystem.core.bi;

import com.google.gson.reflect.TypeToken;
import com.kin.ecosystem.core.data.internal.ConfigurationImpl;
import com.kin.ecosystem.core.network.ApiCallback;
import com.kin.ecosystem.core.network.ApiClient;
import com.kin.ecosystem.core.network.ApiException;
import com.kin.ecosystem.core.network.Pair;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;

class EventsApi {

	private ApiClient apiClient;

	/*
	 * Constructor for EventsApi
	 */
	EventsApi() {
		apiClient = new ApiClient(ConfigurationImpl.getInstance().getEnvironment().getBiUrl());
	}

	/**
	 * Build call for sendEvent
	 *
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call sendEventCall(Event event) throws ApiException {
		Object localVarPostBody = event;

		// create path and map variables
		String localVarPath = "";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();

		final String eventId = apiClient.parameterToString(event.getCommon().getEventId());
		localVarHeaderParams.put("X-REQUEST-ID", eventId);


		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		String[] applicationJson = {ApiClient.APPLICATION_JSON_KEY};
		final String localVarAccept = apiClient.selectHeaderAccept(applicationJson);
		localVarHeaderParams.put("Accept", localVarAccept);

		final String localVarContentType = apiClient.selectHeaderContentType(applicationJson);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		return apiClient
			.buildCall(localVarPath, ApiClient.POST, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, null);
	}

	@SuppressWarnings("rawtypes")
	private Call sendEventValidateBeforeCall(Event event) throws ApiException {
		return sendEventCall(event);
	}

	/**
	 * Send event to BI
	 *
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call sendEventAsync(Event event, final ApiCallback<String> callback)
		throws ApiException {
		Call call = sendEventValidateBeforeCall(event);
		Type localVarReturnType = new TypeToken<String>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}
}
