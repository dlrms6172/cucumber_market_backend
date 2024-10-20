[![Java CI with Gradle](https://github.com/dlrms6172/cucumber_market_backend/actions/workflows/integrate.yml/badge.svg)](https://github.com/dlrms6172/cucumber_market_backend/actions/workflows/integrate.yml)
[![Deploy to Amazon EC2](https://github.com/dlrms6172/cucumber_market_backend/actions/workflows/deploy.yml/badge.svg)](https://github.com/dlrms6172/cucumber_market_backend/actions/workflows/deploy.yml)

# 중고거래 플랫폼 오이마켓 (백엔드)

## 프로젝트 구성
 * 팀원: 백엔드 2명, 프론트엔드 2명
 * 기간: 2024.07 ~

## 스택
 * DB: MySQL
 * SQL Mapper: MyBatis
 * 서버: AWS EC2
 * 클라우드 스토리지: AWS S3
 * 프레임워크: Spring Boot
 * 프로그래밍 언어: Java

## 세부 내용
 * 회원 관리
 * 중고 상품 판매 서비스
 * 거래 후기 서비스
 * 회원의 주소를 기반으로 일정 반경에 있는 상품 조회 서비스

## 기술
 * 복합 키를 이용한 데이터베이스 설계
 * 구글 계정을 이용한 로그인
 * MultiPartFile과 AWS S3를 이용한 상품 이미지 관리
 * 위도, 경도를 이용한 주변 동네와의 거리 계산
 * GitHub Actions를 통한 CI/CD 구축
