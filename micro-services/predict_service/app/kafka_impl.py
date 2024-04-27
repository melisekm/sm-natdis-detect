import asyncio
import json
import logging
import os
import base64

from aiokafka import AIOKafkaConsumer, AIOKafkaProducer

from app.routes import handle_prediction


def encode_json(data: dict) -> bytes:
    return base64.b64encode(json.dumps(data).encode('utf-8'))


async def consume(loop):
    prediction_topic = os.getenv('KAFKA_PREDICTION_TOPIC')
    db_topic = os.getenv('KAFKA_DB_TOPIC')
    kafka_uri = os.getenv('KAFKA_URI')
    if not prediction_topic or not kafka_uri:
        logging.error('KAFKA_PREDICTION_TOPIC or KAFKA_URI is not set')
        return
    while True:
        logging.info('Starting consumer and producer')
        try:
            consumer = AIOKafkaConsumer(
                prediction_topic,
                loop=loop,
                bootstrap_servers=kafka_uri,
            )
            producer = AIOKafkaProducer(
                bootstrap_servers=kafka_uri,
            )

            try:
                await producer.start()
                await consumer.start()
                logging.info('Consumer and producer started. Listening for messages..')
                async for msg in consumer:
                    decoded_msg = msg.value.decode()
                    logging.info(f"Consumed message: {decoded_msg}")
                    predictions = handle_prediction(decoded_msg)
                    logging.info(f"Sent prediction to DB: {predictions}")
                    for prediction in predictions:
                        await producer.send_and_wait(db_topic, encode_json(
                            {
                                'message': decoded_msg,
                                'objectType': 'prediction',
                                'objectBase64': encode_json(prediction.model_dump()).decode()
                            }
                        ))
            except Exception as e:
                logging.exception(f'Error while consuming messages: {e}')
            finally:
                await consumer.stop()
                await producer.stop()
        except Exception as e:
            logging.exception(f'Failed to create consumer and producer. Error: {e}')

        logging.info('Consumer and producer stopped')
        logging.info('Restarting in 10 seconds')
        await asyncio.sleep(10)
