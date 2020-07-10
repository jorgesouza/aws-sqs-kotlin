# AWS SQS and Schedule in Kotlin

The operations is described below.

### QueueController
```
@RestController
@RequestMapping("/queue")
class QueueController(
        @Value("\${cloud.aws.credentials.access-key}") private val accessKey: String,
        @Value("\${cloud.aws.credentials.secret-key}") private val secretKey: String,
        @Value("\${cloud.aws.region.static}") private val awsRegion: String,
        @Value("\${aws.queue.endpoint}") private val queueEndpoint: String,
        @Value("\${aws.queue.queue-test-sqs}") private val queueName: String
) {

    @PostMapping
    fun sendMessage(@RequestBody message: String): ResponseEntity<String> {

        val credentials = BasicAWSCredentials(accessKey, secretKey)

        val sqs = AmazonSQSClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .build()

        val sendMessageStandardQueue = SendMessageRequest()
                .withMessageBody(message)
                .withQueueUrl("$queueEndpoint$queueName")

        val result = sqs.sendMessage(sendMessageStandardQueue)

        return ResponseEntity.ok().body(result.messageId)
    }
}
```

`POST /queue`
```
{
  "name": "test"
}
```

## AWSProvider
```
@Configuration
class AWSProvider(
        @Value("\${cloud.aws.credentials.access-key}") private val accessKey: String,
        @Value("\${cloud.aws.credentials.secret-key}") private val secretKey: String
) {

    @Bean
    fun awsCredentialsProvider(): AWSCredentialsProvider {
        val credentials = BasicAWSCredentials(accessKey, secretKey)

        return AWSStaticCredentialsProvider(credentials)
    }
}
```

## QueueListener
```
@Service
class QueueListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @SqsListener(value = ["queue-test-sqs"])
    fun listener(message: String) {
        logger.info("\nReceived message <> SqsListener: $message")
    }
}
```

## Schedule
```
@Component
@EnableScheduling
class Schedule {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 1000)
    //@Scheduled(cron = "0 0 12 \\* \\* \\*", zone = "America/Sao_Paulo")
    fun receiveMessageSync() {
        logger.info("\nScheduled: ${LocalDateTime.now()}")
    }
}
```
