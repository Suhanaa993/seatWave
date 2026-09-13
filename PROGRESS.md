# SeatWave — Project Progress Log

This file tracks what's been done, in plain language, so I can look back later and explain exactly how the project came together.

---

## Environment Setup (Done)
- Installed JDK 21 (LTS) alongside existing JDK 26; set IntelliJ to use JDK 21 for this project.
- Installed MySQL 8.4 (LTS) with MySQL Command Line Client.
- Created database `seatwave_db` and a dedicated non-root user `seatwave_app` (never use root from the app — good security practice).
- Installed Memurai (a Windows-native, Redis-compatible server) — this stands in for Redis, which doesn't run natively on Windows. Used for temporary seat locks later.
- Installed Postman for testing backend APIs before the frontend exists.

## Phase 1 — Requirements & Architecture (Done)
Decided what "version 1" of the project actually includes, so we don't get lost building too much at once.

**v1 includes:**
- Login/signup with roles (normal user vs admin)
- Admin can create venues, seats, and events
- Users can browse/search events
- Users can see a seat map and lock a seat temporarily while booking
- Booking confirmation and cancellation
- A basic waitlist for sold-out events

**Deliberately saved for later (v1.1+):**
- Live seat-map updates via WebSockets (v1 will just refresh/poll instead — simpler, still works)
- Dynamic pricing
- Kafka-based async processing
- Smarter waitlist ranking
- Docker/deployment packaging

**Basic architecture:**
```
React frontend  <-->  Spring Boot backend  <-->  MySQL (main data)
                                |
                                +--> Redis/Memurai (temporary seat locks only)
```

## Phase 2 — Database Design (In Progress)

Building the database table by table, explaining the reasoning for each column as we go (only add a column if a real feature needs it — no "just in case" fields).

**Tables completed so far:**

1. **`users`** — stores everyone who can log in (both normal users and admins, told apart by a `role` column).
   Columns: id, email, password_hash, full_name, role, created_at, updated_at.

2. **`venues`** — a physical location where events happen (e.g. a stadium).
   Columns: id, name, address, city, total_capacity, created_at, updated_at.
   (`city` gets its own column because search/filter by city is a real v1 feature; `address` stays one text field because nothing needs to query it in pieces.)

**Tables still to design:** seats, events, event_seats, bookings, booking_seats, waitlist_entries.

