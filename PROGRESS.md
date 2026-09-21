Environment Setup (Done)
JDK 21 (LTS), IntelliJ pointed at it.
MySQL 8.4 LTS installed, seatwave_db database + dedicated seatwave_app user created (never use root from the app).
Memurai installed (Windows-native Redis-compatible server) for seat locking later.
Postman installed for API testing.
Phase 1 — Requirements & Architecture (Done)

Locked v1 scope: auth, admin CRUD for venues/seats/events, browsing/search, seat locking via Redis, booking + cancellation, basic waitlist. Deferred to later: WebSockets (v1 uses polling instead), dynamic pricing, Kafka, smarter waitlist ranking, Docker/deployment.

Phase 2 — Database Design (Done)

All 8 tables designed and created by hand (not auto-generated), with real reasoning behind every column: users, venues, seats, events, event_seats (the concurrency-critical one), bookings, booking_seats, waitlist_entries.

Full interview Q&A written up separately (docs/phase2-interview-qa.md) covering normalization decisions, optimistic locking, the DECIMAL vs FLOAT choice, and scalability reasoning.

Phase 3 — Spring Boot Backend Setup (Done)
Project scaffolded via Spring Initializr: Java 21, Spring Boot 3.5.6, Maven.
Worked through several real environment issues (IntelliJ source-root detection, a corrupted pom.xml, Windows Defender slowing IntelliJ down) — all resolved.
Backend successfully starts and connects to MySQL via Spring Data JPA + HikariCP connection pooling.
Phase 4 — Authentication & Authorization (Core done)

Built JWT-based authentication from scratch:

Password hashing — BCrypt via Spring Security's PasswordEncoder, never storing plain-text passwords.
JwtService — generates and validates signed JWT tokens (24-hour expiry).
AuthController — /api/auth/register and /api/auth/login, both tested and working end-to-end (confirmed a real BCrypt hash landed in MySQL, confirmed login returns a valid token).
JwtFilter — runs on every request, validates the token, and tells Spring Security who's making the request.
SecurityConfig — stateless session policy (no server-side sessions, matching token-based auth), open /api/auth/** routes, everything else requires a valid token.

Known rough edge, deliberately deferred: invalid login currently returns a raw 403 instead of a clean 401 with a proper error message. Fixing this needs a global exception handler (@ControllerAdvice), which we're building once Phase 5 generates a few more real error cases, so it can be done once, properly, instead of patched repeatedly.

Phase 5 — CRUD & Core Business Logic (In progress)

Built using a consistent layered pattern for each resource: Entity → Repository → Service → Controller → DTOs, so the same mental model applies everywhere.

Completed and tested end-to-end (confirmed via Postman + direct MySQL checks):

Venue — full CRUD (create/read/update/delete), pagination + city filter. Confirmed a real venue (Wankhede Stadium) landed correctly in MySQL.
Seat — bulk creation (generate many seats for a venue in one call, e.g. rows A-C x 10 seats), rather than one-at-a-time — matches how this would actually be used. Confirmed 30 seats created correctly, linked to their venue via a @ManyToOne relationship.
Event — full CRUD, search by city or category (via a relationship-traversing query, Event -> Venue -> city). Confirmed an event (Coldplay Live) created correctly with nested venue info in the response.

In progress:

EventSeat — the entity connecting a specific seat to a specific event with a live status (AVAILABLE/LOCKED/BOOKED). This is the concurrency-critical table from the Phase 2 design.
Entity created with a @Version field — JPA's built-in optimistic locking mechanism (automatically guards every update with a version check, exactly matching the manual WHERE version = ? reasoning from the Phase 2 interview Q&A).
Repository created.
Still to build: auto-generating EventSeat rows when an event is created, the seat-map viewing endpoint, the Redis-based seat locking service, and the actual booking endpoint that performs the version-checked update.

Remaining in Phase 5: finish EventSeat (Redis locking + booking), then Booking, then WaitlistEntry.