import asyncio
import json
import logging
import os
import base64

from aiokafka import AIOKafkaConsumer, AIOKafkaProducer

from app.routes import handle_dl


def encode_json(data: dict) -> bytes:
    return base64.b64encode(json.dumps(data).encode('utf-8'))


async def consume(loop):
    dl_topic = os.getenv('KAFKA_LINK_TOPIC')
    db_topic = os.getenv('KAFKA_DB_TOPIC')
    kafka_uri = os.getenv('KAFKA_URI')
    if not dl_topic or not kafka_uri:
        logging.error('KAFKA_LINK_TOPIC or KAFKA_URI is not set')
        return
    while True:
        logging.info('Starting consumer and producer')
        try:
            consumer = AIOKafkaConsumer(
                dl_topic,
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
                    base64decoded_msg = msg.value.decode()
                    logging.info(f"Consumed message: {base64decoded_msg}")
                    decoded_msg_dict = json.loads(base64.b64decode(base64decoded_msg).decode())
                    logging.info(f"Decoded message: {decoded_msg_dict}")
                    message = decoded_msg_dict['dataInput']['data']
                    prediction_id = decoded_msg_dict.get('predictionId')
                    extraction_results = handle_dl(message)
                    if not extraction_results:
                        logging.info(f"No links found in message: {message}")
                    else:
                        logging.info(f"Sent link to DB: {extraction_results}")
                        for extraction_result in extraction_results:
                            await producer.send_and_wait(db_topic, encode_json(
                                {
                                    'predictionId': prediction_id,
                                    'objectType': 'link',
                                    'objectBase64': encode_json(extraction_result.model_dump()).decode()
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
