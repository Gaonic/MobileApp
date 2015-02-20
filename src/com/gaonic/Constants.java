package com.gaonic;

public interface Constants {
	public static final String GAONIC_RESPONSE_INVALID_USERID = "GER400001";
	public static final String GAONIC_RESPONSE_INVALID_MAC_ADDRESS_FORMAT = "GER400002";
	public static final String GAONIC_SENSOR_WITH_MAC_ADDRESS_DOESNT_EXISTS = "GER400003";
	public static final String GAONIC_RESPONSE_SENSOR_SUCCESSFULLY_ATTACHED_TO_PROFILE = "GER400004";
	public static final String GAONIC_RESPONSE_SENSOR_ATTACHED_TO_YOUR_PROFILE = "GER400005";
	public static final String GAONIC_RESPONSE_SENSOR_ATTACHED_TO_ANOTHER_PROFILE = "GER400006";
	public static final String GAONIC_RESPONSE_SENSOR_NOTES_SAVED = "GER400008";
	public static final String GAONIC_RESPONSE_SENSOR_NOTES_NOT_SAVED = "GER400009";
	public static final String GAONIC_RESPONSE_INVALID_SENSOR_ID_OR_USER_ID = "GER400007";
	public static final String GAONIC_RESPONSE_SUCCESSFUL_LOGIN = "GER400010";
	public static final String GAONIC_RESPONSE_WRONG_PASSWORD = "GER400011";
	public static final String GAONIC_RESPONSE_USER_NOT_EXIST = "GER400012";
	public static final String GAONIC_RESPONSE_SUCCESSFUL_SIGNUP ="GER400013";
	public static final String GAONIC_RESPONSE_FAILED_SIGNUP = "GER400014";
	
	public static final String GAONIC_URL_SIGNIN = "https://osp.gaonic.com/api/signin_user.json";
	public static final String GAONIC_URL_SIGNUP = "https://osp.gaonic.com/api/new_user.json";
	public static final String GAONIC_URL_USER_SENSORS = "http://osp.gaonic.com/api/user_sensors.json";
	public static final String GAONIC_URL_ATTACH_SENSOR = "https://osp.gaonic.com/api/attach_sensor.json";
	public static final String GAONIC_URL_SAVE_SENSOR_NOTES = "https://osp.gaonic.com/api/save_notes.json";
	public static final String GAONIC_URL_VIEW_SENSOR_NOTES = "http://osp.gaonic.com/api/view_notes.json";//?user_id=1&sensor_id=21";

	
	public static final String GAONIC_KEY_USER_ID = "user_id";
	public static final String GAONIC_KEY_RESPONSE = "response";
	public static final String GAONIC_KEY_MESSAGE = "message";
	
	//public static final String GAONIC_SENSORTYPE_ALL = "all_sensors";
	public static final String GAONIC_SENSORTYPE_MY_SENSORS = "my_sensors";
	public static final String GAONIC_SENSORTYPE_NEARBY_SENSORS = "nearby_sensors";
	
	public static final int REQUEST_MAIN_SCREEN = 0;
	
	public static final String TAG = "Gaonic";
	// attach sensor mac add:  00036aff0101
}
