# sFog source code
Here are the source code of sFog. It includes 2 parts.

One part is fog node. The platform is QT 5.8 framework, installed in the Ubuntu 16.04.3 LTS. We also use the USB wireless adapter to broadcast the wifi signal. Linux tool "create_ap" is used to create the wifi hotspot.

Another part is IoT device. We use smartphone HUAWEI P7 Lite (Android 7.1.1) as the IoT device. We use android studio to create the application.

## System architecture
![system architecture](https://github.com/seamlesshandover1208/sFog-source-code/blob/master/image/system%20architecture.PNG)

## What can support
The prototype can support 18 possible situations and one situation when IoT device moves between 3 fog nodes.
The raw data of evaluation is implemented by this prototype.

## Critical questions
<B>Q: When does IoT device start to disconnect?</B>

A: According to RSS, IoT device starts to connect to new node, at the same time, it disconnects from old node.

<B>Q: How does fog node know when to start  job pre-migration?</B>

A: IoT device will send a signal to fog node to inform old fog 	node it will start to migrate, at the same time, old node will send the result to new node (if exists).

<B>Q: How does IoT device do if it starts to cancel migration after sending pre-migration signal?</B>

A: IoT device will send a signal to fog node to inform old fog 	node it will cancel migration. Old fog node will stop job pre-migration and clear cache.

<B>Q: How does new fog node know which result should be sent back to which IoT device?</B>

A: Each IoT device has unique MAC address, each communication between IoT device and fog node will carry MAC address.
If IoT device connects to new fog node, it will request old tasks (successfully uploaded & not downloaded yet ) to fog node, according to task sequence number. Then fog node will send the results to correct IoT device (if exists).

To conclude, every time IoT devices need to change the status, it will send message to fog node, and fog node respond based on different message.Each fog node needs to communicate with each other to send results or signals.

## System architecture rules
There are maximum 5 pending jobs for each IoT devices on the node.

Use FIFO rule for executing uploading, computing, downloading and request old queue, if queue is not empty, get data from queue. otherwise, just wait.

Both client and node judge successful transmission according to ACK.

When IoT device requests old task results from new node, it is certain that it can get results from node, so it will skip waiting those acks, and starts to upload new task. 

Each queue is handled by specific thread.

At one time instance, there is only 1 uploading job, computing job and downloading job for each specific IoT device.

## Technique
platform: Ubuntu 16.04.3 LTS, login as root

IDE: Qt creator 5.8.0

Codes: C++, C, linux shell script, Qt library

fog node hardware: Dell Latitude E5570, Core i7-6600U CPU 256GB SSD and 8GB memory

IoT device hardware: HUAWEI ALEL02 P8 Lite

Wireless USB adapter: EDiMAX EW-7612UAn V2
