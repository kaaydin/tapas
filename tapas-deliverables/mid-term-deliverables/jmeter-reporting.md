# Performance Evaluation and Optimization through JMeter Testing

![JMeter Analysis Application](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/tapas-at-runtime/tapas-deliverables/mid-term-deliverables/images/JMeter_App.png?raw=true)

In our continuous effort to achieve and maintain high performance standards, we conducted a rigorous Performance Testing using Apache JMeter on both Local and Virtual Machine (VM) setups. 
The primary objective of this exercise was to analyze the behavior and performance characteristics of our services under different load conditions.

We examined the following parameters across varying sample sizes (`#Samples`):

- **Average**: The mean response time, representing the average time taken for requests to be processed.
- **Median**: The median response time, which is the middle value in a series of response times, often a better representation of typical performance as it's not skewed by outliers.
- **90% Line**: 90% of the requests were processed within this time, an indicator of the performance users can expect under typical conditions.
- **95% Line**: 95% of the requests were processed within this time, providing a closer look at potential outliers.
- **99% Line**: 99% of the requests were processed within this time, often used to identify the performance under peak load.
- **Min**: The minimum time taken to process a request, indicating the best-case performance.
- **Max**: The maximum time taken to process a request, indicating the worst-case performance.
- **Error %**: The percentage of requests that resulted in an error, a crucial metric for evaluating the robustness and reliability of the services.
- **Throughput**: The number of requests that can be handled per unit of time, reflecting the capacity and scalability of the services.
- **Received KB/sec**: The rate at which data is received by the server, pertinent for analyzing the network's capacity.
- **Sent KB/sec**: The rate at which data is sent by the server, also crucial for network capacity analysis.

Each of these parameters helped us in diagnosing performance and reliability, forming a basis for our decision-making in futur optimizations and adjustments.

To facilitate a good analysis of the diverse responses, we developed a Streamlit Application available under the following link:
* https://jmeter.streamlit.app/



## Local Environment Using Docker Container

We conducted our performance testing in a local environment utilizing Docker container. The Docker container was configured with a memory usage of 15 GB.

### Testing Scenario 1: Throughput Analysis with POST and GET Task Request

We simulated user load using threads, with each thread representing a user. 
We increased the number of threads (users) and monitored the upper mentioned parameters. 
However, we observed that post reaching 3000 threads, the throughput did not show any further increase. 
Following some points what might could explain this behavior:

![JMeter Analysis Task](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/tapas-at-runtime/tapas-deliverables/mid-term-deliverables/images/JMeter_Task.png?raw=true)

_Explore live analysis here: https://jmeter.streamlit.app/_

- **Resource Limitation**:
    - The Docker container has a fixed amount of memory (15 GB), which might have become exhausted, preventing the system from handling additional requests efficiently.
- **CPU Saturation**:
    - It's possible that the CPU reached its processing capacity. When the CPU is saturated, it can't process requests any faster, which in turn caps the throughput.
- **Concurrency Issues**:
    - As the number of threads increased, the system might have faced contention issues where multiple threads were competing for shared resources, leading to a throughput cap.

## Testing Scenario 2: Test Scenario with Cache Clearance for POST and GET Task Request

We decided to clear all caches by recomposing the Docker container afresh before every test aimed to compare the results with the previous tests where cache was not cleared. 
The average response time was four times higher than before.

![JMeter Analysis Task Clear](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/tapas-at-runtime/tapas-deliverables/mid-term-deliverables/images/JMeter_Task_Clear.png?raw=true)

_Explore live analysis here: https://jmeter.streamlit.app/_

Following some points we discussed to explain this behavior:

- **Cache Efficiency**:
    - Caches store data temporarily so that future requests for that data can be served faster. 
    - When we cleared the cache, every request had to be processed anew, increasing the response time significantly, however, posted a new Task for every test.
- **Cold Starts**:
    - With every new composition of the Docker container the system experienced cold starts where the initial requests take significantly longer to process as the system has to initialize and warm up. Need to do some followup research on that.
- **Database Access**:
    - Without the cache the system likely had to fetch data from the database more frequently. Database accesses are generally much slower compared to cache accesses contributing to the increased response time. Database Image was instantiated new as well, so this should not be the issue. Follow up would be to optimise the logic how we retrieve data from the cache and/or database. 

## Virtual Environment Using VMSwitch

The virtual machine (VM) under test was configured with Ubuntu and had 2 GB RAM. 
Our primary objective remained to gauge the system's performance under varying load conditions and understand how it compares to the local Docker container setup.

### Performance Analysis at Different Load Levels with with POST and GET Task Request

We observed that the VM began exhibiting performance issues as the thread count reached already 1000 threads. The response time soared to a concerning 2'127'847ms alongside a high error rate of 45.2%. In contrast, at 600 threads, the system managed a much lower response time of 155'790ms and a negligible error rate of 1.3%. 
Further, we conducted a test focusing solely on a Task POST request which yielded satisfactory performance up to 500 threads with an average response time of 669ms. However, the response time escalated rapidly with the increase in threads.

![JMeter Analysis VM 600](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/tapas-at-runtime/tapas-deliverables/mid-term-deliverables/images/JMeter_VM_600.png?raw=true)

![JMeter Analysis VM 1000](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/tapas-at-runtime/tapas-deliverables/mid-term-deliverables/images/JMeter_VM_1000.png?raw=true)

_Explore live analysis here: https://jmeter.streamlit.app/_


Some considerations we discussed on the observed behavior:

- **Insufficient Resources**:
  - With only 2 GB RAM, the VM had significantly lesser memory compared to the Docker container. This limited memory could quickly get exhausted as the thread count increased, leading to high response times and error rates.
- **CPU Limitation**:
  - The VM’s CPU resources might have reached a saturation point, unable to handle the increasing number of concurrent requests efficiently, leading to a rapid increase in response time and errors.
- **Concurrency Overheads**:
  - At higher thread counts, the contention for shared resources and synchronization overheads could have exacerbated the response times and error rates.

### Comparative Wrap-Up: Local vs Virtual Environment

The performance outcomes from the local Docker container and the VM it’s evident that resource allocation plays an important role in handling load efficiently:

- Both environments showcased a threshold beyond which the performance deteriorated. However, the threshold was notably higher in the local Docker environment owing to the higher memory allocation of 15 GB compared to the 2 GB in the VM.
- The error rates and response times were markedly lower in the local environment at comparable thread counts, indicating a better handling of concurrency and resource management.

The underlying issues in both environments seem to orbit around resource limitations and concurrency management. 
However, the local environment with the Docker container exhibited a higher resilience and capability in handling a larger load compared to the VM setup. 

## Possible Future Work 
Identified issue for enhancement is the logic surrounding data retrieval from cache and/or database. 
We need to analyze and optimize this logic further to achieve significantly reduced response times and increased throughput. 
Thereby ensuring a seamless user experience even under high load conditions.

Some theoretical points we could further discuss on:

**Cache Optimization**

- Reduce Database Load

- Consistency Maintenance

**Database Access Optimization**

- Query Efficiency
  
- Indexing

- Connection Pooling

**Hybrid Data Retrieval Strategy**

- Cache-Aside Pattern

- Read-Through and Write-Through Caching



