mock_prediction = {
    'informative': {
        'text': 'Hurricane Ida is expected to hit New York and Manhattan at 4am today. The storm is moving at 200km/h. '
                '#StaySafe #HurricaneIda https://www.cnn.com/2021/08/30/weather/hurricane-ida-monday/index.html',
        'label': 'Informative',
        'confidence': 0.97,
        'entities': {
            'locations': [
                'New York',
                'Manhattan',
            ],
            'date_time': [
                '4am', 'today',
            ],
            'entities': [
                'Hurricane Ida',
            ],
            'other': [
                '200km'
            ]
        },
        'links': [
            {
                'link': 'https://www.cnn.com/2021/08/30/weather/hurricane-ida-monday/index.html',
                'title': 'Hurricane Ida: New Orleans braces for Category 4 storm',
                'has_entities': True,
            },
        ]
    },
    'non-informative': {
        'text': 'I love my dog so much. He is my best friend. #doglovers #mansbestfriend',
        'label': 'Non-Informative',
        'confidence': 0.99,
        'entities': {},
        'links': [],
    },
}
