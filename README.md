# Multithreaded Web Server Project

A Java-based web server implementation demonstrating different threading approaches for handling multiple client connections. This project includes three different server implementations: Single-threaded, Multi-threaded, and Thread Pool-based servers, designed for performance testing with JMeter.

## 📁 Project Structure

```
MultithreadedWebServer/
├── SingleThreaded/
│   ├── Server.java
│   └── Client.java
├── MultiThreaded/
│   ├── Server.java
│   └── Client.java
└── ThreadPool/
    ├── Server.java
    └── Client.java
```

## 🚀 Features

- **Single-threaded Server**: Basic server implementation that handles clients sequentially
- **Multi-threaded Server**: Creates a new thread for each client connection
- **Thread Pool Server**: Uses a bounded thread pool with work queue for efficient resource management
- **Client Applications**: Test clients for each server implementation with concurrent connections
- **Performance Testing**: Optimized for JMeter load testing with 1000 threads per second
- **Graceful Shutdown**: All servers support graceful shutdown with Ctrl+C

## 🛠️ Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache JMeter (for load testing)
- Basic understanding of Java networking and threading concepts

## 📋 Requirements

- Java 8+
- No external dependencies required (uses only Java standard library)
- Apache JMeter 5.0+ (for performance testing)

## 🏃‍♂️ How to Run

### Single-Threaded Server

1. **Compile the server:**
   ```bash
   cd SingleThreaded
   javac Server.java
   ```

2. **Run the server:**
   ```bash
   java Server
   ```

3. **In another terminal, compile and run the client:**
   ```bash
   javac Client.java
   java Client
   ```

### Multi-Threaded Server

1. **Compile the server:**
   ```bash
   cd MultiThreaded
   javac Server.java
   ```

2. **Run the server:**
   ```bash
   java Server
   ```

3. **In another terminal, compile and run the client:**
   ```bash
   javac Client.java
   java Client
   ```

### Thread Pool Server

1. **Compile the server:**
   ```bash
   cd ThreadPool
   javac Server.java
   ```

2. **Run the server:**
   ```bash
   java Server
   ```

3. **In another terminal, compile and run the client:**
   ```bash
   javac Client.java
   java Client
   ```

## 🔧 Implementation Details

### Single-Threaded Server
- **Port**: 8010
- **Behavior**: Handles one client at a time sequentially
- **Use Case**: Simple applications with low concurrency requirements
- **Limitations**: Can only serve one client simultaneously
- **Features**: 
  - 30-second socket timeout
  - Graceful shutdown support
  - Proper resource cleanup

### Multi-Threaded Server
- **Port**: 8010
- **Behavior**: Creates a new thread for each client connection
- **Use Case**: Applications requiring high concurrency
- **Features**: 
  - Handles multiple clients simultaneously
  - Each client gets its own dedicated thread
  - 30-second socket timeout
  - Daemon threads for graceful shutdown
  - Proper resource cleanup

### Thread Pool Server
- **Port**: 8010
- **Thread Pool Size**: 20 threads
- **Queue Capacity**: 100 requests
- **Behavior**: Uses bounded thread pool with work queue
- **Use Case**: Production-ready applications with controlled resource usage
- **Features**: 
  - Bounded thread pool prevents resource exhaustion
  - Work queue with CallerRunsPolicy for backpressure
  - 30-second socket timeout
  - Active thread monitoring
  - Graceful shutdown with timeout
  - Proper resource cleanup

### Client Applications
- **Single-threaded Client**: Simple client that connects once
- **Multi-threaded Client**: Creates 100 concurrent connections to test server performance
- **Thread Pool Client**: Uses thread pool to create 100 concurrent connections

## 📊 Performance Characteristics

| Implementation | Concurrency | Resource Usage | Scalability | JMeter Performance |
|----------------|-------------|----------------|-------------|-------------------|
| Single-threaded | Low | Minimal | Limited | ~1 TPS |
| Multi-threaded | High | High | Good | ~500-800 TPS |
| Thread Pool | High | Moderate | Excellent | ~800-1000+ TPS |

## 🧪 Testing

### Manual Testing
Each client creates multiple concurrent connections to test server performance:

```bash
cd ThreadPool
java Client
```

This will create 100 threads, each establishing a connection to the server simultaneously.

### JMeter Load Testing

#### Setup JMeter Test Plan

1. **Create Thread Group:**
   - Number of Threads: 1000
   - Ramp-up period: 10 seconds
   - Loop Count: 1

2. **Add TCP Sampler:**
   - Server Name: localhost
   - Port: 8010
   - Send: `Hello from JMeter Client\n`
   - Response Timeout: 5000ms

3. **Add Listeners:**
   - View Results Tree
   - Aggregate Report
   - Response Time Graph

#### Expected Performance Results

**Single-threaded Server:**
- Throughput: ~1-5 TPS
- Response Time: High (queuing)
- Error Rate: High under load

**Multi-threaded Server:**
- Throughput: ~500-800 TPS
- Response Time: Moderate
- Error Rate: Low to moderate

**Thread Pool Server:**
- Throughput: ~800-1000+ TPS
- Response Time: Low
- Error Rate: Very low

### Expected Output

**Server Output:**
```
ThreadPool Server is listening on port 8010
Thread pool size: 20
Queue capacity: 100
Press Ctrl+C to stop the server
New client connected: /127.0.0.1 (Active threads: 1)
Client [/127.0.0.1] says: Hello from Client 0 [/127.0.0.1:54321]
```

**Client Output:**
```
Starting 100 concurrent clients...
Client 0 received: Hello Client! -- from the ThreadPool Server
Client 1 received: Hello Client! -- from the ThreadPool Server
...
All clients completed.
```

## 🔍 Key Concepts Demonstrated

1. **Socket Programming**: Basic client-server communication using Java sockets
2. **Threading Models**: Different approaches to handling concurrent connections
3. **Resource Management**: How different threading models affect resource usage
4. **Network Programming**: TCP/IP communication fundamentals
5. **Performance Optimization**: Thread pool configuration for high throughput
6. **Load Testing**: JMeter integration for performance validation

## 🚧 Future Enhancements

- [x] Implement Thread Pool server with bounded queue
- [x] Add graceful shutdown support
- [x] Improve error handling and resource cleanup
- [x] Optimize for JMeter testing
- [ ] Add HTTP protocol support
- [ ] Implement request/response handling
- [ ] Add configuration file support
- [ ] Implement logging and monitoring
- [ ] Add unit tests
- [ ] Performance benchmarking tools
- [ ] Docker containerization

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

Created as a learning project for understanding multithreading concepts in Java networking applications.

## 📚 Additional Resources

- [Java Socket Programming](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [Java Threading](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Network Programming in Java](https://docs.oracle.com/javase/tutorial/networking/)
- [Apache JMeter Documentation](https://jmeter.apache.org/usermanual/index.html)
- [Thread Pool Best Practices](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html)

---

**Note**: This project is designed for educational purposes to understand different threading approaches in server applications. The Thread Pool implementation is optimized for JMeter testing with 1000 threads per second. For production use, consider using established frameworks like Spring Boot or Netty. 