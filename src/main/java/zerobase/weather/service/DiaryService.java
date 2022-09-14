package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)   // 아래에있는 메서드 들이 트랜잭션으로 동작한다.
public class DiaryService {
    // Controller 에서 createDiary에 있는 LocalDate date, String text 두개를 Service에서 구현
    @Value("${openweathermap.key}")
    private String apikey;
    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    // 매일 자정 1시에 (초 분 시 일 월) 이 데이터 값을 전달해준다.
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        logger.info("날씨를 잘 가지고 옴");
        // dateWeather -> Entity값을 가지고 와야한다. api를 통해서 매일 1시에 날씨 데이터값을 가지고 오기 위해
        dateWeatherRepository.save(getWeatherFromApi());
    }

    // 날씨일기 쓰는 작업
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createDiary(LocalDate date, String text) {
        logger.info("started to create diary");

        // 날씨 데이터 가져오기 (API 에서 가져오기? or DB 에서 가져오기?)
        DateWeather dateWeather = getDateWeather(date);

        // 3. 파싱된 데이터 + 일기 값 우리 db에 넣기
        // Mysql에 diary 테이블 추가 작성하고 -> 시작!
        Diary nowDiary = new Diary();
        // DateWeather 를 가지고 와서 Diary에 메서드 생성 해서 가지고 오는 부분, 코드가 수월해진다.
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
        logger.info("end to create diary");
    }

    // 날씨는 매일 1번 가지고 오는 부분 (매번 날씨 api 호출 저장하는 부분)
    private DateWeather getWeatherFromApi() {
        // 1. open weather map에서 날씨 데이터 가져오기
        String weatherDate = (getWeatherString());
        // 2. 받아온 날씨 json 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherDate);
        DateWeather dateWeather = new DateWeather();

        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parseWeather.get("main").toString());
        dateWeather.setIcon(parseWeather.get("icon").toString());
        dateWeather.setTemperature((Double)parseWeather.get("temp"));
        return dateWeather;

    }
    // 날씨 데이터 가져오기 (API 에서 가져오기? or DB 에서 가져오기?) --> createDiary() 메서드에 있는
    // 지금 이 방법은 DB에서 가져오는 방법
    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherListFromDB.size() == 0) {
            // 새로 api에서 날씨 정보를 가져와야한다.
            // 정책상,, 현재 날씨를 가져오도록 할 수 있고,, 날씨없이 일기를 쓰도록,,
            return getWeatherFromApi();
        }else {
            return dateWeatherListFromDB.get(0);
        }
    }

    // 날씨조회 -> Repository에서 날씨일기를 읽어들어올 함수를 생성해주고 date값을 가지고 온다.
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
//        if (date.isAfter(LocalDate.ofYearDay(3050,1))){
////            throw new InvalidDate();
//        }
        return diaryRepository.findAllByDate(date);
    }

    // 날씨일기 범위 전체조회
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate){
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    // 날씨일기 수정
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        // setText로 지정을 해서 수정하게끔 한다. 날씨일기 수정
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    // 날씨일기 삭제
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apikey;
        try{

            // 1. open weather 날씨데이터 가져오는 부분 url -> Http 식으로 연결시켜주는 작업
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // GET 방식으로 요청을 보냈다
            connection.setRequestMethod("GET");
            // responseCode -> 200, 300, 400 서버가 어떤 응답 코드를 주는지 확인
            int responseCode = connection.getResponseCode();
            // 응답 값들을 br에 담아서, 읽어 들인다.
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        }catch (Exception e){
            return "failed to get response";
        }
    }

    // 2. 받아온 날씨 json 파싱하기 String 값으로 가져오기
    // 파싱하기 json
    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject; // Object 객체 생성

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
        Map<String, Object> resultMap = new HashMap<>();

        // main -> 안에 있는 temp도 같이 가져와야 하니깐, https://openweathermap.org/current 여기 사이트에서 3가지를 가져오기 위해서 main, temp, weather
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));
        return resultMap;
    }
}
