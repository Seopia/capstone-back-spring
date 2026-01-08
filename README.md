# 🧠 Refill (심리 케어 서비스)

> **"당신의 마음을 기록하고, AI와 함께 성장하세요."** > 사용자의 대화 패턴과 감정을 분석하여 개인화된 심리 리포트를 제공하고,  
> 커뮤니티를 통해 서로를 위로하는 멘탈 케어 플랫폼입니다.

---

## 📖 프로젝트 개요 (Overview)
현대인의 정신 건강 관리를 돕기 위해 개발된 본 서비스는 **LLM 기반의 AI 챗봇**과 **사용자 커뮤니티**를 결합했습니다.
단순한 대화를 넘어, 사용자의 언어적 표현을 분석하여 **3가지 핵심 성장 지표(긍정성, 안정도, 자신감)**를 시각화하여 제공함으로써 사용자가 스스로의 마음 상태를 객관적으로 파악하고 관리할 수 있도록 돕습니다.

---

## 🛠 기술 스택 (Tech Stack)

### Backend
| Category | Technology |
| --- | --- |
| **Framework** | Spring Boot 3.2.4 |
| **Language** | Java 17 |
| **Security** | Spring Security, JWT, OAuth2 (Kakao) |
| **Database** | **MySQL** (User/Community), **MongoDB** (Chat Logs) |
| **Communication** | WebSocket (STOMP), REST API |
| **AI Integration** | LangChain (LLM Service) |

### Frontend
| Category | Technology |
| --- | --- |
| **Framework** | React |
| **Styling** | Styled-components (or CSS Framework) |
| **Communication** | Axios, SockJS |

### Infrastructure
| Category | Technology |
| --- | --- |
| **Server** | AWS EC2 (Ubuntu) / Raspberry Pi |
| **Build Tool** | Gradle |

---

## ✨ 주요 기능 (Key Features)

### 1. 💬 AI 심리 상담 챗봇
* **실시간 대화**: WebSocket(STOMP)을 이용한 지연 없는 실시간 채팅 환경 구현
* **감정 분석**: 대화 내용을 기반으로 사용자의 감정을 분석하고 요약 리포트 생성

### 2. 📊 개인화된 성장 지표 (Growth Metrics)
단순 평균이 아닌, 사용자의 **최근 상태와 변화 추세**를 반영하는 독자적인 알고리즘을 적용했습니다.

* **긍정성 (Positivity)**
    * 지수 가중 이동 평균(EMA, $\alpha=0.2$)을 적용
    * 과거의 기록보다 **최근의 기분**이 점수에 더 크게 반영되도록 설계
* **안정도 (Stability)**
    * 감정 점수의 변동성(표준편차)을 역산하여 계산
    * 최근 데이터일수록 변동성에 페널티를 부여하여, **꾸준한 심리 상태** 유지 시 고득점
* **자신감 (Confidence)**
    * 긍정성과 안정도의 조화를 기반으로 산출
    * 최근 3건의 데이터가 '상승세'일 경우 **성장 보너스(+10점)** 부여 로직 적용

### 3. 🏘️ 치유 커뮤니티 (Forum)
* 일기 형식의 게시글 작성 및 감정 공유
* 타 사용자와의 소통(댓글, 좋아요)을 통한 정서적 지지
* 게시글 정렬(최신순, 좋아요순, 댓글순) 기능 제공

### 4. 🔐 사용자 편의성
* **Kakao 소셜 로그인** 연동으로 간편한 가입 절차
* JWT (Access/Refresh Token) 기반의 안전한 보안 인증

---

## 📂 프로젝트 구조 (Package Structure)

```bash
com.website
├── board           # 커뮤니티(게시글, 댓글) 도메인
├── chat            # 채팅 서비스 및 감정 분석 로직 (MongoDB 연동)
├── config          # WebSocket, Security, Web 설정
├── entity          # JPA(MySQL) 및 MongoDB Document 엔티티
├── repository      # Data Access Layer (JPA & MongoRepository)
├── security        # JWT 필터 및 OAuth2 핸들러
├── user            # 회원 관리 및 카카오 로그인
└── websocket       # 소켓 메시지 브로커 설정
```

---

## 🏗 시스템 아키텍처 (System Architecture)

본 프로젝트는 데이터의 특성에 따라 RDBMS와 NoSQL을 혼용하는 **Hybrid Database 전략**을 채택했습니다.

```mermaid
graph TD
    subgraph Client ["Client Side"]
        FE[("🖥️ React Frontend")]
    end

    subgraph Server ["Backend Server (AWS EC2)"]
        direction TB
        SB[("🍃 Spring Boot Application")]
        
        subgraph Core_Logic ["Core Services"]
            Auth[Security / JWT]
            Chat[Chat Service & WebSocket]
            Board[Board Service]
            Analysis[Emotion Analysis Logic]
        end
    end

    subgraph Data_Storage ["Hybrid Database"]
        MySQL[("🐬 MySQL (RDBMS)<br/>- User Info<br/>- Board/Comment<br/>- Analysis Results")]
        Mongo[("🍃 MongoDB (NoSQL)<br/>- Chat Logs<br/>- Conversation History")]
    end

    subgraph External ["External Services"]
        Kakao[("💬 Kakao OAuth")]
        LLM[("🤖 LangChain / LLM")]
    end

    %% Flow Connections
    FE -- "REST API (HTTP)" --> Auth
    FE -- "WebSocket (STOMP)" --> Chat
    
    SB --> Core_Logic
    
    Auth -- "Social Login" --> Kakao
    Chat -- "Prompting" --> LLM
    
    Board -- "JPA" --> MySQL
    Analysis -- "JPA" --> MySQL
    Chat -- "MongoRepository" --> Mongo
