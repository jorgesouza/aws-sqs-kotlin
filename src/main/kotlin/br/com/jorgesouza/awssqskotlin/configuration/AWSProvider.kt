package br.com.jorgesouza.awssqskotlin.configuration

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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