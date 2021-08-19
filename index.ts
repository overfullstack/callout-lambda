import * as pulumi from "@pulumi/pulumi";
import * as aws from "@pulumi/aws";
import {RolePolicyAttachment} from "@pulumi/aws/iam";

const role = new aws.iam.Role("callout-role", {
  assumeRolePolicy: `{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
`
});

new RolePolicyAttachment("callout-dynamodb-full-access-policy",
  {
    role: role,
    policyArn: aws.iam.ManagedPolicies.AmazonDynamoDBFullAccess
  });

new RolePolicyAttachment("callout-lambda-basic-policy",
  {
    role: role,
    policyArn: aws.iam.ManagedPolicies.AWSLambdaBasicExecutionRole
  });

const lambdaFunction = new aws.lambda.Function("callout-lambda", {
  code: new pulumi.asset.FileArchive("build/distributions/callout-lambda.zip"),
  handler: "org.revcloud.Lambda",
  role: role.arn,
  memorySize: 512,
  timeout: 30,
  runtime: "java11"
});

const logGroupApi = new aws.cloudwatch.LogGroup("callout-api-route", {
  name: "callout-lambda",
});

const apiGatewayPermission = new aws.lambda.Permission("callout-gateway-permission", {
  action: "lambda:InvokeFunction",
  "function": lambdaFunction.name,
  principal: "apigateway.amazonaws.com"
});

const api = new aws.apigatewayv2.Api("callout-api", {
  protocolType: "HTTP"
});

const pokemonTable = new aws.dynamodb.Table("Pokemon", {
  name: "Pokemon",
  attributes: [
    {name: "Id", type: "S"},
    {name: "Count", type: "N"},
  ],
  hashKey: "Id",
  rangeKey: "Count",
  readCapacity: 1,
  writeCapacity: 1,
});

const calloutLogTable = new aws.dynamodb.Table("CalloutLog", {
  name: "CalloutLog",
  attributes: [
    {name: "Id", type: "S"},
  ],
  hashKey: "Id",
  readCapacity: 1,
  writeCapacity: 1,
});

const apiDefaultStage = new aws.apigatewayv2.Stage("default", {
  apiId: api.id,
  autoDeploy: true,
  name: "$default",
  accessLogSettings: {
    destinationArn: logGroupApi.arn,
    format: `{"requestId": "$context.requestId", "requestTime": "$context.requestTime", "httpMethod": "$context.httpMethod", "httpPath": "$context.path", "status": "$context.status", "integrationError": "$context.integrationErrorMessage"}`
  }
})

const lambdaIntegration = new aws.apigatewayv2.Integration("callout-api-lambda-integration", {
  apiId: api.id,
  integrationType: "AWS_PROXY",
  integrationUri: lambdaFunction.arn,
  payloadFormatVersion: "1.0"
});

const apiDefaultRole = new aws.apigatewayv2.Route("callout-api-route", {
  apiId: api.id,
  routeKey: `$default`,
  target: pulumi.interpolate`integrations/${lambdaIntegration.id}`
});

export const publishedUrl = apiDefaultStage.invokeUrl;
