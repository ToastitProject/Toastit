package alcoholboot.toastit.feature.trendcocktail.service.impl;

import alcoholboot.toastit.feature.trendcocktail.entity.TrendCocktail;
import alcoholboot.toastit.feature.trendcocktail.repository.TrendCocktailRepository;
import alcoholboot.toastit.feature.trendcocktail.service.TrendCocktailService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrendCocktailServiceImpl implements TrendCocktailService {

    private final RestTemplate restTemplate;
    private final TrendCocktailRepository trendCocktailRepository;

    @Value("${api.naver.client-id}")
    private String clientId;

    @Value("${api.naver.client-secret}")
    private String clientSecret;

    @Transactional
    public void save(TrendCocktail trendCocktail) {
        trendCocktailRepository.save(trendCocktail);
    }

    @Override
    public String getSearchVolume(List<String> keywords) {
        // 현재 날짜
        LocalDate now = LocalDate.now();

        // 저번 달의 시작일과 종료일 계산
        LocalDate firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth());

        // 저저번 달의 시작일과 종료일 계산
        LocalDate firstDayOfTwoMonthsAgo = now.minusMonths(2).withDayOfMonth(1);
        LocalDate lastDayOfTwoMonthsAgo = now.minusMonths(2).withDayOfMonth(now.minusMonths(2).lengthOfMonth());

        // 날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateLastMonth = firstDayOfLastMonth.format(formatter);
        String endDateLastMonth = lastDayOfLastMonth.format(formatter);

        String startDateTwoMonthsAgo = firstDayOfTwoMonthsAgo.format(formatter);
        String endDateTwoMonthsAgo = lastDayOfTwoMonthsAgo.format(formatter);

        String url = "https://openapi.naver.com/v1/datalab/search"; // API URL (네이버 데이터랩 트렌드)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.set("Content-Type", "application/json");

        StringBuilder finalResponse = new StringBuilder();

        // 키워드를 5개씩 나누어 요청
        for (int i = 0; i < keywords.size(); i += 5) {
            List<String> sublist = keywords.subList(i, Math.min(i + 5, keywords.size()));

            // 키워드 그룹 생성
            StringBuilder keywordGroupBuilder = new StringBuilder();
            keywordGroupBuilder.append("\"keywordGroups\":[");

            for (int j = 0; j < sublist.size(); j++) {
                keywordGroupBuilder.append("{\"groupName\":\"").append(sublist.get(j)).append("\",\"keywords\":[\"").append(sublist.get(j)).append("\"]}");
                if (j < sublist.size() - 1) {
                    keywordGroupBuilder.append(",");
                }
            }

            keywordGroupBuilder.append("]");

            // 요청 본문 생성
            String requestBody = String.format("{\"startDate\":\"%s\",\"endDate\":\"%s\",\"timeUnit\":\"month\",%s}",
                    startDateTwoMonthsAgo, endDateLastMonth, keywordGroupBuilder.toString());

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답 추가
            finalResponse.append(response.getBody());

            // 마지막 응답이 아닐 경우 || 추가 -> 객체 생성 시 응답 본문을 || 로 구분
            if (i + 5 < keywords.size()) {
                finalResponse.append("||");
            }
        }

        return finalResponse.toString();
    }

    @Override
    public List<TrendCocktail> findTop5BySearchVolume() {
        return trendCocktailRepository.findTop5BySearchVolume().stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}