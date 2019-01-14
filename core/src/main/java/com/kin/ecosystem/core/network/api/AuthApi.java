/*
 * Kin Ecosystem
 * Apis for client to server interaction
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.kin.ecosystem.core.network.api;


import static com.kin.ecosystem.core.network.ApiClient.PATCH;

import com.google.gson.reflect.TypeToken;
import com.kin.ecosystem.core.data.internal.ConfigurationImpl;
import com.kin.ecosystem.core.network.ApiCallback;
import com.kin.ecosystem.core.network.ApiClient;
import com.kin.ecosystem.core.network.ApiException;
import com.kin.ecosystem.core.network.ApiResponse;
import com.kin.ecosystem.core.network.Pair;
import com.kin.ecosystem.core.network.model.AccountInfo;
import com.kin.ecosystem.core.network.model.JWT;
import com.kin.ecosystem.core.network.model.UserProfile;
import com.kin.ecosystem.core.network.model.UserProperties;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;


public class AuthApi {

	private ApiClient apiClient;

	public AuthApi() {
		this(ConfigurationImpl.getInstance().getDefaultApiClient());
	}

	public AuthApi(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	/**
	 * Build call for signIn
	 *
	 * @param jwt (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call signInCall(JWT jwt, String X_REQUEST_ID) throws ApiException {
		Object localVarPostBody = jwt;

		// create path and map variables
		String localVarPath = "/users";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, ApiClient.POST, localVarQueryParams, localVarCollectionQueryParams,
				localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call signInValidateBeforeCall(JWT jwt, String X_REQUEST_ID) throws ApiException {

		// verify the required parameter 'jwt' is set
		if (jwt == null || jwt.isEmpty()) {
			throw new ApiException("Missing the required parameter 'jwt' when calling signIn(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling signIn(Async)");
		}

		return signInCall(jwt, X_REQUEST_ID);


	}

	/**
	 * Sign in/ Log in
	 * Sign a user into kin marketplace
	 *
	 * @param jwt (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return AccountInfo
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public AccountInfo signIn(JWT jwt, String X_REQUEST_ID) throws ApiException {
		ApiResponse<AccountInfo> resp = signInWithHttpInfo(jwt, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * Sign in/ Log in
	 * Sign a user into kin marketplace
	 *
	 * @param jwt (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;AccountInfo&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<AccountInfo> signInWithHttpInfo(JWT jwt, String X_REQUEST_ID) throws ApiException {
		Call call = signInValidateBeforeCall(jwt, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<AccountInfo>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * Sign in/ Log in (asynchronously)
	 * Sign a user into kin marketplace
	 *
	 * @param jwt (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call signInAsync(JWT jwt, String X_REQUEST_ID, final ApiCallback<AccountInfo> callback)
		throws ApiException {

		Call call = signInValidateBeforeCall(jwt, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<AccountInfo>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}


	/**
	 * Build call for getUserProfile
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call userProfileCall(String X_REQUEST_ID) throws ApiException {
		Object localVarPostBody = null;

		// create path and map variables
		String localVarPath = "/users/me";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {

		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient.buildCall(localVarPath, ApiClient.GET, localVarQueryParams, localVarCollectionQueryParams,
			localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call userProfileValidateBeforeCall(String X_REQUEST_ID) throws ApiException {

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling getUserProfile(Async)");
		}

		return userProfileCall(X_REQUEST_ID);
	}

	/**
	 * Get user profile
	 * Get user profile
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return UserProfile
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public UserProfile userProfile(String X_REQUEST_ID) throws ApiException {
		ApiResponse<UserProfile> resp = userProfileWithHttpInfo(X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * Get user profile
	 * Get user profile
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;UserProfile&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<UserProfile> userProfileWithHttpInfo(String X_REQUEST_ID) throws ApiException {
		Call call = userProfileValidateBeforeCall(X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<UserProfile>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * Get user profile (asynchronously)
	 * Get user profile
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call userProfileAsync(String X_REQUEST_ID, final ApiCallback<UserProfile> callback) throws ApiException {

		Call call = userProfileValidateBeforeCall(X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<UserProfile>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}


	/**
	 * Build call for hasAccount
	 *
	 * @param userId (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call hasAccountCall(String userId, String X_REQUEST_ID) throws ApiException {
		Object localVarPostBody = userId;

		// create path and map variables
		String localVarPath = "/users/exists";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (userId != null) {
			localVarQueryParams.addAll(apiClient.parameterToPair("user_id", userId));
		}
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, ApiClient.GET, localVarQueryParams, localVarCollectionQueryParams,
				localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call hasAccountValidateBeforeCall(String userId, String X_REQUEST_ID) throws ApiException {

		// verify the required parameter 'user_id' is set
		if (userId == null) {
			throw new ApiException("Missing the required parameter 'userId' when calling hasAccount(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling hasAccount(Async)");
		}

		return hasAccountCall(userId, X_REQUEST_ID);
	}

	/**
	 * Has Account
	 * Check whether a account is associated to the userId
	 *
	 * @param userId (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Boolean
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public Boolean hasAccount(String userId, String X_REQUEST_ID) throws ApiException {
		ApiResponse<Boolean> resp = hasAccountWithHttpInfo(userId, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * Has Account
	 * Check whether is an associated account to the userId
	 *
	 * @param userId (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;Boolean&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<Boolean> hasAccountWithHttpInfo(String userId, String X_REQUEST_ID) throws ApiException {
		Call call = hasAccountValidateBeforeCall(userId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<Boolean>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * Has Account (asynchronously)
	 * Check whether a account is associated to the userId
	 *
	 * @param userId (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call hasAccountAsync(String userId, String X_REQUEST_ID, final ApiCallback<Boolean> callback)
		throws ApiException {

		Call call = hasAccountValidateBeforeCall(userId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<Boolean>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Build call for updateUser
	 *
	 * @param userproperties (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call updateUserCall(UserProperties userproperties) throws ApiException {
		Object localVarPostBody = userproperties;

		// create path and map variables
		String localVarPath = "/users/me";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, PATCH, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call updateUserValidateBeforeCall(UserProperties userproperties) throws ApiException {

		// verify the required parameter 'userproperties' is set
		if (userproperties == null) {
			throw new ApiException("Missing the required parameter 'userproperties' when calling updateUser(Async)");
		}

		Call call = updateUserCall(userproperties);
		return call;
	}

	/**
	 * Update user (asynchronously)
	 * Update user - wallet address
	 *
	 * @param userproperties (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call updateUserAsync(UserProperties userproperties, final ApiCallback<Void> callback) throws ApiException {
		Call call = updateUserValidateBeforeCall(userproperties);
		apiClient.executeAsync(call, callback);
		return call;
	}

	/**
	 * Build call for updateUser
	 *
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	private Call logoutCall() throws ApiException {
		// create path and map variables
		String localVarPath = "/users/me/session";

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();

		final String[] localVarAccepts = {
			"application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, ApiClient.DELETE, null, null, null,
				localVarHeaderParams, null, localVarAuthNames);
	}

	/**
	 * Update user (asynchronously)
	 * Update user - wallet address
	 *
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call logoutAsync(final ApiCallback<Void> callback) throws ApiException {
		Call call = logoutCall();
		apiClient.executeAsync(call, callback);
		return call;
	}

}
