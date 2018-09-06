package com.techm.adopt.service;


import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.util.Constants;

public class ProjectReportsServiceImpl implements ProjectReportsService {
	final static Logger LOGGER = Logger
			.getLogger(ProjectReportsServiceImpl.class);
	Map<String, String> map = null;
	Map<String, JSONObject> sprintMap = null;
	String projectKey;
	boolean flag = false;
	List versionsList = null;
	Map<String, String> releaseMap = null;
	private HashMap<String, JSONObject> versionsMap = new HashMap<String, JSONObject>();
	
	@Override
	public JSONArray getSprintDetails(String projectname) throws JSONException,
			UnsupportedEncodingException, SQLException {
		
		List<String> list = getSprintIds(projectname);
		JSONArray jsonarray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject json = getSingleSprintDetails(getBoardId(projectname), list.get(i));
			jsonarray.put(json);
		}
		return jsonarray;
	}
	//swapna
	@Override
	public JSONObject getSprintsIssuesDetails(String projectname) throws JSONException,
			UnsupportedEncodingException, SQLException {
		flag = true;
		map = new HashMap<String, String>();
		sprintMap = new HashMap<String, JSONObject>();
		String sprintName =null;
		List<String> list = getSprintIds(projectname);
		for (int i = 0; i < list.size(); i++) {
			JSONObject json = getIssueDetails(getBoardId(projectname), list.get(i));
			for (Map.Entry entry : map.entrySet())
			{
				if(entry.getKey().equals(list.get(i))){
					sprintName = (String) entry.getValue();
				}
			}
			sprintMap.put(sprintName, json);
		}
		JSONObject result = new JSONObject(sprintMap);
		return result;
	}
	@Override
	public JSONArray getTestTrend(String projectname) throws JSONException,
			UnsupportedEncodingException, SQLException {

		return null;
	}

	@Override
	public String getOutputFromUrl(String url) throws SQLException {
		String username = Constants.JIRA_USERNAME;
		String password = Constants.JIRA_PASSWORD;
		
		Client client = Client.create();
		String auth = new String(Base64.encode(username + ":" + password));
		String output = null;
		try {
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource
					.header("Authorization", "Basic " + auth)
					.type("application/json").accept("application/json")
					.get(ClientResponse.class);
			int statusCode = response.getStatus();
			if (statusCode == 401) {
				throw new AuthenticationException(
						"Invalid Username or Password");
			}
			output = response.getEntity(String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	@Override
	public List<ProjectDetailsBean> getProjectDetails() throws JSONException,
			UnsupportedEncodingException, SQLException {

		return null;
	}

	@Override
	public ProjectDetailsBean getStoryDetails(String projectname)
			throws UnsupportedEncodingException, JSONException, SQLException {

		return null;
	}

	public String getProjectStartDate(String projectname) throws SQLException,
			JSONException, UnsupportedEncodingException {

		return null;
	}

	/**
	 * This is sprint burndown not project burndown in Jira API implementation. For VersionOne Implemetaiton it is 
	 * ProjectBurnDown.
	 */
	
	@Override
	public JSONArray getSprintBurnDown(String projectname)
			throws SQLException, JSONException, ParseException,
			UnsupportedEncodingException {
		//String url = "http://172.19.82.13:8050/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId=2&sprintId=2";
		String rapidViewId = getBoardId(projectname);
		String url= Constants.SCOPE_BURNDOWN+"?rapidViewId="+rapidViewId+"&sprintId=34";
		String output = getOutputFromUrl(url);
		JSONObject json = new JSONObject(output);
		String startdate = json.get("startTime").toString();
		String lastdate = json.get("endTime").toString();

		List<String> list = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Iterator<String> itr1 = json.getJSONObject("changes").keys();
		while (itr1.hasNext()) {
			list.add(itr1.next());
		}
		Collections.sort(list);

		Iterator<String> itr = list.iterator();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = null;
		while (itr.hasNext()) {
			String date = itr.next();
			String actualDate = getFormatedDate(date);
			JSONArray array = json.getJSONObject("changes").getJSONArray(date);
			json1 = getJsonofDateandValues(array, json1, json2, actualDate);
		}

		JSONArray jsonarray = getFormatedJsonArrayDate(json1);
		return buildBurndownData(jsonarray, startdate, lastdate);
	}
//swapna
	public List<String> getSprintIds(String ProjectName) throws SQLException, JSONException {
		String boardId = getBoardId(ProjectName);
		/*String url = "http://172.19.82.13:8050/rest/agile/1.0/board/"
				+ boardId + "/sprint";*/
		String url=Constants.BOARD+ boardId + "/sprint";
		String output = getOutputFromUrl(url);
		JSONObject json = new JSONObject(output);
		JSONArray jsonArray = json.getJSONArray("values");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject sprintidJson = jsonArray.getJSONObject(i);
			String id = sprintidJson.get("id").toString();
			list.add(id);
			if(flag){
			map.put(id, sprintidJson.get("name").toString());
			}
		}
		return list;
	}
//swapna
	public String getBoardId(String ProjectName) throws SQLException, JSONException {
		//String url = "http://172.19.82.13:8050/rest/greenhopper/1.0/rapidviews/list";
		String url=Constants.BOARDID_LIST;
		String output = getOutputFromUrl(url);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("views");
		String id = null;
		//String projectname = "ADOPT";
		//String projectname = "OSA board";
		for (int i = 0; i < array.length(); i++) {
			if(id != null){
				break;
			}
			JSONObject jsonprojects = array.getJSONObject(i);
			JSONObject object = jsonprojects.getJSONObject("filter")
					.getJSONObject("queryProjects");
			if(object.has("displayMessage")){
				System.out.println("display message");
			}
			JSONArray jsonarray = null;
			try{
			jsonarray = object.getJSONArray("projects");
			}catch(Exception e){
				System.out.println("There is no projects array from rest api");
			}
			
			
			for (int j = 0; j < jsonarray.length(); j++) {
				JSONObject json1 = jsonarray.getJSONObject(j);
				String pname = json1.getString("name");
				if (pname.equals(ProjectName)) {
					id = jsonprojects.get("id").toString();
					projectKey = json1.getString("key");
					break;
				}
				
			}
			}
		
		return id;
		//return "35";
	}

	public String getFormatedDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(date));
		return sdf.format(calendar.getTime());

	}

	public JSONArray buildBurndownData(JSONArray jsonarray, String startdate,
			String lastdate) throws JSONException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int i = 0;
		String date1 = startdate;
		JSONArray array = new JSONArray();
		String lDate = String.valueOf(Long.parseLong(lastdate)
				- (24 * 60 * 60 * 1000));
		while (Long.parseLong(lDate) > Long.parseLong(date1)) {
			JSONObject jsonfinal = new JSONObject();
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(getFormatedDate(startdate)));
			cal.add(Calendar.DAY_OF_MONTH, i);
			date1 = sdf.format(cal.getTime());
			int count2 = 0;
			for (int j = 0; j < jsonarray.length(); j++) {
				JSONObject object = jsonarray.getJSONObject(j);
				try {
					if (object.getString("date").equals(date1)) {
						jsonfinal.put("date", date1);
						jsonfinal.put("estimate", object.get("estimate")
								.toString());
						array.put(jsonfinal);
						count2++;
					}
				} catch (Exception e) {
				}

			}
			if (count2 == 0 ) {
				jsonfinal.put("date", date1);
				jsonfinal.put("estimate", "0");

				array.put(jsonfinal);
			}
			i++;
			Date d = sdf.parse(date1);
			long timestamp = d.getTime();
			date1 = String.valueOf(timestamp);
		}
		return array;
	}

	public JSONObject getJsonofDateandValues(JSONArray array, JSONObject json1,
			JSONObject json2, String actualDate) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			if (object.has("timeC")) {
				String a = object.getJSONObject("timeC").get("newEstimate")
						.toString();
				String key = object.getString("key");
				if (json1.has(actualDate)) {
					json2 = new JSONObject(json1.getJSONObject(actualDate)
							.put(key, a).toString());
				} else {
					if (null != json2) {
						json2 = new JSONObject(json2.toString());
						json1.put(actualDate, json2.put(key, a));

					} else {
						json2 = new JSONObject();
						json1.put(actualDate, json2.put(key, a));
					}
				}
			}
		}
		return json1;
	}

	public JSONArray getFormatedJsonArrayDate(JSONObject json)
			throws JSONException {
		JSONArray jsonarray = new JSONArray();
		@SuppressWarnings("unchecked")
		Iterator<String> dateIterator = json.keys();
		while (dateIterator.hasNext()) {
			JSONObject newjson = new JSONObject();
			String date = dateIterator.next().toString();

			@SuppressWarnings("unchecked")
			Iterator<String> keyIterator = json.getJSONObject(date).keys();
			int count = 0;
			while (keyIterator.hasNext()) {
				String value = keyIterator.next().toString();
				count = count
						+ Integer.parseInt(json.getJSONObject(date).getString(
								value));
			}
			newjson.put("date", date);
			newjson.put("estimate", count);
			jsonarray.put(newjson);
		}
		return jsonarray;
	}

	public JSONObject getSingleSprintDetails(String rapidViewId, String sprintId)
			throws SQLException, JSONException {
		/*String url = "http://172.19.82.13:8050/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId="
				+ rapidViewId + "&sprintId=" + sprintId;*/
		String url=Constants.SPRINT_LIST+"?rapidViewId="
				+ rapidViewId + "&sprintId=" + sprintId;
		String output = getOutputFromUrl(url);
		JSONObject jsonSprint = new JSONObject();
		JSONObject json = new JSONObject(output);
		
		String completedIssuesEstimateSum = json.getJSONObject("contents")
				.getJSONObject("completedIssuesEstimateSum").getString("text");
		String issuesNotCompletedEstimateSum = json.getJSONObject("contents")
				.getJSONObject("issuesNotCompletedEstimateSum")
				.getString("text");
		if ("null".equals(issuesNotCompletedEstimateSum)) {
			issuesNotCompletedEstimateSum = "0";
		}
		if ("null".equals(completedIssuesEstimateSum)) {
			completedIssuesEstimateSum = "0";
		}
		String allIssuesEstimateSum = String.valueOf(Float
				.parseFloat(completedIssuesEstimateSum)
				+ Float.parseFloat(issuesNotCompletedEstimateSum));

		String sprintStartDate = json.getJSONObject("sprint").get("startDate")
				.toString();
		String sprintEndDate = json.getJSONObject("sprint").get("endDate")
				.toString();
		String sprintName = json.getJSONObject("sprint").get("name").toString();

		if (sprintEndDate.equals("None")) {
			sprintEndDate = "Not Defined";
		}
		if (sprintStartDate.equals("None")) {
			sprintStartDate = "Not Defined";
		}

		jsonSprint.put("storypointstotal", allIssuesEstimateSum);
		jsonSprint.put("storypointsclosed", completedIssuesEstimateSum);
		jsonSprint.put("startdate", sprintStartDate);
		jsonSprint.put("enddate", sprintEndDate);
		jsonSprint.put("sprintname", sprintName);

		return jsonSprint;
	}
	//swapna
	public JSONObject getIssueDetails(String rapidViewId, String sprintId)
			throws SQLException, JSONException {
		String url=Constants.SPRINT_LIST+"?rapidViewId="
				+ rapidViewId + "&sprintId=" + sprintId;
		//String url= "http://10.53.67.57:8888/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=37&sprintId=38";
		String output = getOutputFromUrl(url);
		Map map = new HashMap<String, List<JSONObject>>();
		JSONObject jsonSprint1 = new JSONObject(output);
		JSONObject obj = jsonSprint1.getJSONObject("contents");
		JSONArray completeIssuesarray = obj.getJSONArray("completedIssues");
		List list = new ArrayList<JSONObject>();
		List list1 = new ArrayList<JSONObject>();
		List list2 = new ArrayList<JSONObject>();
		for(int i=0;i<completeIssuesarray.length();i++){
			JSONObject jsonIssue = new JSONObject();
			jsonIssue.put("issueName", completeIssuesarray.getJSONObject(i).getString("key"));
			jsonIssue.put("issueSummary", completeIssuesarray.getJSONObject(i).getString("summary"));
			jsonIssue.put("issueStatus", completeIssuesarray.getJSONObject(i).getString("statusName"));
			try{
			jsonIssue.put("issueAssign", completeIssuesarray.getJSONObject(i).getString("assigneeName"));
			}catch(Exception e){
				System.out.println("assign name is not available in rest api of Jira.");
				jsonIssue.put("issueAssign", "");
			}
			list.add(jsonIssue);
		}
		JSONArray arraynotCompleted = obj.getJSONArray("issuesNotCompletedInCurrentSprint");
		for(int i=0;i<arraynotCompleted.length();i++){
			JSONObject jsonIssue = new JSONObject();
			jsonIssue.put("issueName", arraynotCompleted.getJSONObject(i).getString("key"));
			jsonIssue.put("issueSummary", arraynotCompleted.getJSONObject(i).getString("summary"));
			jsonIssue.put("issueStatus", arraynotCompleted.getJSONObject(i).getString("statusName"));
			//jsonIssue.put("issueAssign", arraynotCompleted.getJSONObject(i).getString("assigneeName") != null?arraynotCompleted.getJSONObject(i).getString("assigneeName"):"");
			try{
				jsonIssue.put("issueAssign", completeIssuesarray.getJSONObject(i).getString("assigneeName"));
				}catch(Exception e){
					System.out.println("assign name is not available in rest api of Jira.");
					jsonIssue.put("issueAssign", "");
				}
			if((arraynotCompleted.getJSONObject(i).getString("statusName")).equals("To Do")){
				list1.add(jsonIssue);
			}
			if((arraynotCompleted.getJSONObject(i).getString("statusName")).equals("In Progress")){
				list2.add(jsonIssue);
			}
			
		}
		map.put("completedIssues", list);
		map.put("NotStratedIssues", list1);
		map.put("InProgressIssues", list2);
		JSONObject jsonResult = new JSONObject(map);
		return jsonResult;
	}
	
	/***
	 * In release burndown, Jira api(http://172.19.82.13:8050/rest/greenhopper/1.0/rapid/charts/releaseburndownchart?rapidViewId=2&versionId="+releaseId)
	 *  is giving details of all changes(work added) and all sprints.
	 * I was not able to find a api or logic which can filter the data based on sprints. 
	 * Whatever I found was not correct way of implementation. Therefore there is no logic currently 
	 * implemented to filter the release data and "Release 2" has been hard coded i.e "ID=10001". 
	 * This part of the code is(was) implemented in my last days of TechM so, didn't find much time
	 * to think on this implementation. Also, please the comment section of "calWorkAdded()" method.     
	 */

	@Override
	public JSONObject releaseBurndown(String releaseId) throws SQLException, JSONException {
		String url = Constants.RELEASE_BURNDOWN+"?rapidViewId=35&versionId="+releaseId;
		String output = getOutputFromUrl(url);
		JSONObject versionJson = new JSONObject(output);

		JSONArray sprintsJsonArray = versionJson.getJSONArray("sprints");

		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<String, String> idSprintnameMap = new HashMap<String, String>();
		Map<String, Map<String, String>> issuemap = new HashMap<String, Map<String, String>>();
		Map<String, Integer> versionMap = preProcessReleaseScopeChange(versionJson);
		List<String> list = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Iterator<String> itr = new JSONObject(versionMap).keys();
		while(itr.hasNext()) {
			list.add(itr.next());
		}
		Collections.sort(list);
		for (int i = 0; i < sprintsJsonArray.length(); i++) {
			JSONObject sprintJsonObject = sprintsJsonArray.getJSONObject(i);

			String sprintId = sprintJsonObject.get("id").toString();
			String sprintName = sprintJsonObject.get("name").toString();
			Long sprintStartTime = sprintJsonObject.getLong("startTime");
			Long sprintEndTime = sprintJsonObject.getLong("endTime");
			String state = sprintJsonObject.getString("state");
			Long previousSprintStartTime = 0l;
			String previousSprintId = null;
			if (i != 0) {
				previousSprintStartTime = sprintsJsonArray.getJSONObject(i - 1)
						.getLong("startTime");
				previousSprintId = sprintsJsonArray.getJSONObject(i - 1)
						.get("id").toString();
			} else {
				previousSprintStartTime = 0l;
				previousSprintId = "0";
			}

			calWorkAdded(new JSONObject(versionMap), sprintStartTime, sprintEndTime, sprintId,
					previousSprintStartTime, map, previousSprintId, list);
			idSprintnameMap.put(sprintId, sprintName);
			getOutput(sprintId, issuemap, sprintName, state, releaseId);
		}
		return calculateReleaseBurndown(map, issuemap);
	}

	/**calWorkAddded method calculates how much of work was added during each sprint. As explained in the previous comment
	 * (Refer comment section of "releaseBurndown()" method), Jira api is giving all work added details and in the same api 
	 * it gives details about all sprints. calWorkAdded method calculates how much of work was added during each sprint.
	 * Therefore we have to loop the Result obtained from Jira api(Refer comment section of "releaseBurndown()" method) 
	 * each time. For ex: if we have M sprints and N changes for work added then we have to loop  M * N times. Tough 
	 * worst case runtime for this implementation is N^2 if we have small cout of sprints the current implementation is fine.
	 * However if we have large number of sprints for a project, this implementation won't work as N^2 will be too huge.
	 * It's better to implement binary search algorithm here.
	 */
	
	public void calWorkAdded(JSONObject versionMap, Long sprintStartTime,
			Long sprintEndTime, String sprintId, Long previousSprintStartTime,
			Map<String, Integer> map, String previousSprintId, List<String> list)
			throws JSONException {
		Iterator<String> itr = list.iterator();
		int workadded = 0;
		int previousworkadded = 0;
		int first = 0;
		int last = list.size() - 1;
		int middle = (first + last)/2;
	/*	while( first <= last ) {
			Long date = Long.parseLong(list.get(middle));
	      if ( date <  sprintEndTime ){
	    	  first = middle + 1; 
	    	  if(date > sprintStartTime) {
	    		  int count = versionMap.getInt(String.valueOf(date));
	    		  workadded = workadded + count;
	    	  } else if (date > previousSprintStartTime && date < sprintStartTime) {
					int count = versionMap.getInt(String.valueOf(date));
					previousworkadded = previousworkadded + count;
				}
	      } else {
	         last = middle - 1;
	         if(date > sprintStartTime) {
	    		  int count = versionMap.getInt(String.valueOf(date));
	    		  workadded = workadded + count;
	    	  } else if (date > previousSprintStartTime && date < sprintStartTime) {
					int count = versionMap.getInt(String.valueOf(date));
					previousworkadded = previousworkadded + count;
				}
	      }
	 
	      middle = (first + last)/2;
	   }*/
		while (itr.hasNext()) {
			Long date = Long.parseLong(itr.next());
			if (date > sprintStartTime && date < sprintEndTime) {
				int count = versionMap.getInt(String.valueOf(date));
				workadded = workadded + count;

			} else if (date > previousSprintStartTime && date < sprintStartTime) {
				int count = versionMap.getInt(String.valueOf(date));
				previousworkadded = previousworkadded + count;
			}
		}
		map.put(sprintId, workadded);
		if (previousworkadded > 0) {
			map.put(previousSprintId, previousworkadded);
		}
	}

	private JSONObject calculateReleaseBurndown(Map<String, Integer> map,
			Map<String, Map<String, String>> issuemap) throws JSONException {

		JSONObject firstmap = new JSONObject(map);
		JSONObject secondmap = new JSONObject(issuemap);
		@SuppressWarnings("unchecked")
		Iterator<String> itr = firstmap.keys();
		List<Integer> list = new ArrayList<Integer>();
		
		/***
		 * hard coded part 
		 * 
		 */
		while (itr.hasNext()) {
			int id = Integer.parseInt(itr.next());
			if (id > 11) { //this is hard coding in this report. 
				list.add(id);
			}
		}
		Collections.sort(list);
		JSONObject burndownsprintnamejson = new JSONObject();
		ListIterator<Integer> itr1 = list.listIterator();
		int count = 0;
		List<Integer> listworkrem = new ArrayList<Integer>();
		while (itr1.hasNext()) {
			JSONObject burndownestimatejson = new JSONObject();
			String sprintid = String.valueOf(itr1.next());
			if (secondmap.has(sprintid)) {
				int workadded = firstmap.getInt(sprintid);
				int workcompleted = 0;
				int worknotcompleted = 0;

				workcompleted = secondmap.getJSONObject(sprintid).getInt(
						"completedIssuesEstimateSum");
				worknotcompleted = secondmap.getJSONObject(sprintid).getInt(
						"issuesNotCompletedEstimateSum");

				int workremaining = 0;
				if (listworkrem.size() > 0) {
					String previousSprintId = String.valueOf(list
							.get(count - 1));

					workremaining = firstmap.getInt(previousSprintId)
							+ listworkrem.get(count - 1) - workcompleted;
					listworkrem.add(workremaining);

				} else {
					workremaining = worknotcompleted;
					listworkrem.add(workremaining);
				}
				burndownestimatejson.put("workadded", workadded);
				burndownestimatejson.put("workcompleted", workcompleted);
				burndownestimatejson.put("workremaining", workremaining);
				burndownestimatejson.put("sprintstate", secondmap
						.getJSONObject(sprintid).getString("sprintstate"));
				burndownestimatejson.put(
						"sprintname",
						secondmap.getJSONObject(sprintid).getString(
								"sprintName"));
				burndownsprintnamejson.put(sprintid, burndownestimatejson);
			}
			count++;
		}
		return burndownsprintnamejson;
	}

	public void getOutput(String sprintId,
			Map<String, Map<String, String>> issuemap, String sprintName,
			String state, String releaseId) throws SQLException, JSONException {
		String url = Constants.SPRINT_LIST+"?rapidViewId=35&sprintId="
				+ sprintId;
		String output = getOutputFromUrl(url);
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = new JSONObject(output).getJSONObject("contents");
		JSONArray completedissueArary = json.getJSONArray("completedIssues");
		int completedIssuesEstimateSum = 0;
		int issuesNotCompletedEstimateSum = 0;
		for (int i = 0; i < completedissueArary.length(); i++) {
			try {
				JSONObject completedIssueJson = completedissueArary
						.getJSONObject(i);
				int estimate = completedIssueJson
						.getJSONObject("estimateStatistic")
						.getJSONObject("statFieldValue").getInt("value");
				JSONArray fixversionarray = completedIssueJson
						.getJSONArray("fixVersions");
				for (int j = 0; j < fixversionarray.length(); j++) {
					String fixversionJson = fixversionarray.get(j).toString();
					if (releaseId.equals(fixversionJson)) {
						completedIssuesEstimateSum = completedIssuesEstimateSum
								+ estimate;
					}
				}
			} catch (Exception e) {
				map.put("completedIssuesEstimateSum", "0");
				map.put("issuesNotCompletedEstimateSum", "0");
				map.put("sprintName", sprintName);
				map.put("sprintstate", state);
				issuemap.put(sprintId, map);
			}

		}
		JSONArray notcompletedissueArary = json
				.getJSONArray("issuesNotCompletedInCurrentSprint");

		for (int i = 0; i < notcompletedissueArary.length(); i++) {
			try {
				JSONObject notcompletedIssueJson = notcompletedissueArary
						.getJSONObject(i);
				int estimate = notcompletedIssueJson
						.getJSONObject("estimateStatistic")
						.getJSONObject("statFieldValue").getInt("value");
				JSONArray fixversionarray = notcompletedIssueJson
						.getJSONArray("fixVersions");
				for (int j = 0; j < fixversionarray.length(); j++) {
					String fixversionJson = fixversionarray.get(j).toString();

					if (releaseId.equals(fixversionJson)) {
						issuesNotCompletedEstimateSum = issuesNotCompletedEstimateSum
								+ estimate;
					}
				}
			} catch (Exception e) {
				map.put("issuesNotCompletedEstimateSum", "0");
				map.put("sprintName", sprintName);
				map.put("sprintstate", state);
				issuemap.put(sprintId, map);
			}
		}

		map.put("completedIssuesEstimateSum",
				String.valueOf(completedIssuesEstimateSum));
		map.put("issuesNotCompletedEstimateSum",
				String.valueOf(issuesNotCompletedEstimateSum));
		map.put("sprintName", sprintName);
		map.put("sprintstate", state);
		issuemap.put(sprintId, map);

	}
	
	private Map<String, Integer> preProcessReleaseScopeChange(
			JSONObject versionJson) throws JSONException {
		JSONObject dateJson = versionJson.getJSONObject("changes");
		@SuppressWarnings("unchecked")
		Iterator<String> itr = dateJson.keys();
		Map<String, Integer> map = new HashMap<String, Integer>();
		while (itr.hasNext()) {
			Long date = Long.parseLong(itr.next());
			JSONArray array = dateJson.getJSONArray(String.valueOf(date));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				try {
					int count = obj.getJSONObject("statC").getInt("newValue");
					map.put(String.valueOf(date), count);
				} catch (Exception e) {
				}
			}
		}
		return map;
	}
	
	public void calIssueCompletedAndIssueNotcompleted(JSONArray issueArary, String sprintId,
			Map<String, Map<String, String>> issuemap, String sprintName,
			String state, Map<String, String> map, String releaseId) {
			int issuesNotCompletedEstimateSum = 0;
		for (int i = 0; i < issueArary.length(); i++) {
			try {
				JSONObject notcompletedIssueJson = issueArary
						.getJSONObject(i);
				int estimate = notcompletedIssueJson
						.getJSONObject("estimateStatistic")
						.getJSONObject("statFieldValue").getInt("value");
				JSONArray fixversionarray = notcompletedIssueJson
						.getJSONArray("fixVersions");
				for (int j = 0; j < fixversionarray.length(); j++) {
					String fixversionJson = fixversionarray.get(j).toString();

					if (releaseId.equals(fixversionJson)) {
						issuesNotCompletedEstimateSum = issuesNotCompletedEstimateSum
								+ estimate;
					}
				}
			} catch (Exception e) {
				map.put("issuesNotCompletedEstimateSum", "0");
				map.put("sprintName", sprintName);
				map.put("sprintstate", state);
				issuemap.put(sprintId, map);
			}
		}

	}
	@Override
	public JSONArray getReleaseVersions(String projectName) throws SQLException, JSONException{
		getBoardId(projectName);
		versionsList= new ArrayList();
		releaseMap = new HashMap<String ,String>();
		JSONObject object = null;
		Map map = new HashMap<String, String>();
		String url = Constants.LATEST_PROJECT+"/"+projectKey+"/versions";
		String output = getOutputFromUrl(url);
		JSONArray array= new JSONArray(output);
		if(array != null){
			for(int i=0;i<array.length();i++){
				JSONObject jsonVersion = new JSONObject();
				object = array.getJSONObject(i);
				versionsMap.put(object.get("name").toString(), object);
				versionsList.add(object.get("id"));
				//releaseMap.put(object.get("name").toString(), object.get("released").toString());
				
			}
		  }
		JSONObject result = new JSONObject(versionsMap);
		return array;
	}
	@Override
	public JSONObject getReleasesIds(String projectName) throws SQLException, JSONException{
		getReleaseVersions(projectName);
		JSONObject releaseIds = new JSONObject(versionsList);
		return releaseIds; 
	}
	public JSONArray getAllProjectNames() throws SQLException, JSONException{
		String url = Constants.PROJECT_LIST;
		List<JSONObject> allProjList= new ArrayList<JSONObject> ();
		String response = getOutputFromUrl(url);
		JSONArray array= new JSONArray(response);
		return array;
		
	}
	public  List<String> getProjectName(){
		JSONArray array;
		List<String> list = new ArrayList<String>();
		try {
			array = getAllProjectNames();
			if(array != null){
				for(int i=0;i<array.length();i++){
					JSONObject object = array.getJSONObject(i);
					list.add(object.get("name").toString());
				}}
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	@Override
	public JSONObject getAllDetailsofProject() throws SQLException, JSONException, UnsupportedEncodingException{
		JSONObject result = new JSONObject();
		JSONArray array=getAllProjectNames();
		Map issueMap = null;
		Map releaseMap = new HashMap<String , JSONObject>();
		JSONObject releasePro = new JSONObject(releaseMap);
		if(array != null){
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				String projectName = object.get("name").toString();
				JSONArray releaseArray = getReleaseVersions(projectName);
				String rapidViewId = getBoardId(projectName);
				JSONObject issuerelease = new JSONObject();
				for(int j=0;j<releaseArray.length();j++){
					JSONObject releaseObject = releaseArray.getJSONObject(j);
					String releaseName = releaseObject.get("name").toString();
					String url = Constants.RELEASE_BURNDOWN+"?rapidViewId="+rapidViewId+"&versionId="+releaseObject.get("id");
					String output = getOutputFromUrl(url);
					JSONObject versionJson = new JSONObject(output);
					JSONArray sprintsJsonArray = versionJson.getJSONArray("sprints");
					JSONObject sprintJsonObject =null;
					issueMap = new HashMap<String, JSONObject>();
					for (int k = 0; k < sprintsJsonArray.length(); k++) {
						sprintJsonObject = sprintsJsonArray.getJSONObject(k);
						String sprintId = sprintJsonObject.get("id").toString();
						String sprintName = sprintJsonObject.get("name").toString();
						String state = sprintJsonObject.getString("state");
						JSONObject issueobj = getIssueDetails(rapidViewId, sprintId);
						issueobj.put("sprintState", state);
						issueMap.put(sprintName, issueobj);
				        }
					//issueMap.put("releaseStatus", releaseObject.get("released").toString());
					JSONObject releasesinfo = new JSONObject(issueMap);
					issuerelease.put(releaseName, releasesinfo);
					//releaseMap.put(releaseName, issuesSprint);
					}
				releasePro.put(projectName, issuerelease);
			}
			
		}
		return releasePro;
	}
	
	@Override
	public JSONObject getAllProjects() throws JSONException, SQLException{
		//String url = "http://10.53.67.57:8888/rest/api/2/project";
		List<JSONObject> allProjList= new ArrayList<JSONObject> ();
		JSONObject result = new JSONObject();
		Map<String, JSONObject> vmap = null;
		JSONArray array=getAllProjectNames();
			//String response = getOutputFromUrl(url);
			//JSONArray array= new JSONArray(response);
			
			if(array != null){
				JSONObject eachproj = null;
				for(int i=0;i<array.length();i++){
					vmap = new HashMap<String, JSONObject>();
					 eachproj = new JSONObject();
					JSONObject object = array.getJSONObject(i);
					String projectName = object.get("name").toString();
					projectKey = object.get("key").toString();
					JSONArray releases = getReleaseVersions(projectKey);
					int noOfRleases = releases.length();//versionsList.size();
					JSONObject json = null;
					for(int j=0;j<releases.length();j++){
						json = new JSONObject();
						JSONObject obj = releases.getJSONObject(j);
						json.put("versionName", obj.get("name"));
						json.put("status", obj.get("released"));
						try{
						json.put("releaseDate", obj.get("releaseDate"));
						}catch(Exception e){
							json.put("releaseDate","");
							System.out.println("release date not specified");
						}
						vmap.put((String) obj.get("name"), json);
					}
					String projectStatus = null;
					int completed = 0;
					int incomplete = 0;
					for(String key : vmap.keySet()){
						JSONObject jvalue = vmap.get(key);
						if(jvalue.get("status").equals(true)){
							completed++;
						}else{incomplete++;
						}
					}
					
					if(completed>0 && incomplete==0){
						projectStatus = "Completed";
					}else if(completed>=0 && incomplete>0 || !(vmap.isEmpty())){
						projectStatus = "InProgress";
					}else if(completed==0 && incomplete==0){
						projectStatus = "Not Started";
					}
					JSONObject releasesinfo = new JSONObject(vmap);
					eachproj.put("projectName", projectName);
					eachproj.put("noOfRleases", noOfRleases);
					eachproj.put("releases", releasesinfo);
					eachproj.put("projectStatus", projectStatus);
					//allProjList.add(eachproj);
					result.put(projectName, eachproj);
				}
				
			  }
			return result;
	}
	
	public String getResponse(String url) throws SQLException,
			JSONException {
		String username = Constants.JIRA_SERVICE_DESK_USERNAME;
		String password = Constants.JIRA_SERVICE_DESK_PASSWORD;
		Client client = Client.create();
		String auth = new String(Base64.encode(username + ":" + password));
		String output = null;
		try {
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.type("application/json")
									.header("X-ExperimentalApi", true).header("Authorization", "Basic " + auth)
									.accept("application/json").get(ClientResponse.class);
			output = response.getEntity(String.class);

	} catch (Exception e) {
		e.printStackTrace();
	}
		return output;
	}
	
	public Map<String,String> getprojJiraandJiraserviceDesk() throws SQLException, JSONException{
		List jiraList = getProjectName();
		Map<String,String> map = new HashMap<String,String>();
		String url = Constants.JIRA_SERVICE_DESK_SERVICES;
		String output = getResponse(url);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("values");
		JSONObject result = new JSONObject();
		String id = null;
		for(int j=0;j<jiraList.size();j++){
		for(int i=0;i<array.length();i++){
			JSONObject object = array.getJSONObject(i);
			String name = object.get("projectName").toString();
			if(name.equalsIgnoreCase((String) jiraList.get(j))){
				id = object.get("id").toString();
				map.put(name, id);
			}
		}
		}
		//map.put("test1", "34");
		return map;
	}
	@Override
	public JSONObject getOpenIssuesofProject() throws SQLException,
			JSONException {
		JSONObject result = new JSONObject();
		String queueName = "Code Fix Request";
		Map<String,String> commonMap = getprojJiraandJiraserviceDesk();
		for(Map.Entry<String, String> entry : commonMap.entrySet()){
		String queueUrl = Constants.JIRA_SERVICE_DESK_SERVICES+"/"+entry.getValue()+"/queue";
		String output1 = getResponse(queueUrl);
		JSONObject queueJson = new JSONObject(output1);
		JSONArray queueArray = queueJson.getJSONArray("values");
		String queueId = null;
		for(int i=0;i<queueArray.length();i++){
			JSONObject object = queueArray.getJSONObject(i);
			String name = object.get("name").toString();
			if(name.equals(queueName)){
				queueId = object.get("id").toString();
			}
		}
		String issueUrl = queueUrl+"/"+queueId+"/issue";
		String issuesOutput = getResponse(issueUrl);
		JSONObject issuesJson = new JSONObject(issuesOutput);
		JSONArray issuesArray = issuesJson.getJSONArray("values");
		result.put(entry.getKey(), issuesArray);
		}
		return result;
	}

	
}
