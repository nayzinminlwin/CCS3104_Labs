# Deadlock Demonstration with Socket Programming

This project demonstrates a classic **deadlock scenario** using client-server socket programming in Java with synchronized blocks.

## Overview

The server manages two shared resources (**Resource A** and **Resource B**). Multiple clients can connect and request these resources in different orders:
- **ORDER1**: Acquire A → then B
- **ORDER2**: Acquire B → then A

When two clients execute different orders simultaneously, a **circular wait** condition occurs, causing a **deadlock**.

## Files

- `Server.java` - Multi-threaded server managing two resources with synchronized blocks
- `Client.java` - Client that connects to server and requests resources

## How to Run

### Step 1: Compile
```bash
cd d:\UPM\2ndYear\1stSem\CCS3104_AdvJava\Labs\myLabFiles\HelloFX\src
javac Chap5/deadlock/*.java
```

### Step 2: Start the Server
```bash
java Chap5.deadlock.Server
```

### Step 3: Start Multiple Clients (in separate terminals)
```bash
# Terminal 1
java Chap5.deadlock.Client

# Terminal 2  
java Chap5.deadlock.Client
```

## How to Trigger Deadlock

1. **In Client 1**: Type `ORDER1` and press Enter
2. **IMMEDIATELY in Client 2**: Type `ORDER2` and press Enter
3. **Result**: Both clients will **hang indefinitely** (deadlock!)

## Why Deadlock Occurs

**Circular Wait Condition:**

| Time | Client 1 (ORDER1)        | Client 2 (ORDER2)        |
|------|--------------------------|--------------------------|
| T1   | Locks Resource A ✓       | Locks Resource B ✓       |
| T2   | Waits for Resource B ⏳   | Waits for Resource A ⏳   |
| T3+  | **DEADLOCK** 💀           | **DEADLOCK** 💀           |

- Client 1 holds **A**, needs **B**
- Client 2 holds **B**, needs **A**
- Neither can proceed → **Circular wait** → **Deadlock**

## Server Output During Deadlock

```
[Client 1] ✓ Acquired Resource A
[Client 1] Waiting for Resource B...
[Client 2] ✓ Acquired Resource B
[Client 2] Waiting for Resource A...
(both stuck forever)
```

## Key Concepts Demonstrated

1. **Mutex/Lock** - Uses `synchronized` blocks for mutual exclusion
2. **Hold and Wait** - Each client holds one resource while waiting for another
3. **Circular Wait** - Two clients create a cycle in the resource dependency graph
4. **No Preemption** - Resources cannot be forcibly taken away

## How to Prevent This Deadlock

1. **Lock Ordering** - Always acquire resources in the same order (e.g., always A → B)
2. **Timeouts** - Use `tryLock()` with timeout instead of blocking forever
3. **Deadlock Detection** - Monitor thread states and break cycles
4. **Resource Hierarchy** - Assign a total ordering to all resources

## Testing Without Deadlock

To verify the system works without deadlock:
- Run both clients with the **same order** (both ORDER1 or both ORDER2)
- They will execute sequentially without deadlock
