mock_prediction = {
    'informative': {
        'id': 1,
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
                'published_at': '2021-08-30T12:00:00Z',
                'has_entities': True,
                'entities': {
                    'locations': [
                        'New Orleans',
                    ],
                    'entities': [
                        'Hurricane Ida',
                    ],
                },
                "description": '''
                Editor’s Note: Is your power out or device’s battery charge low? Bookmark CNN’s lite site for text-only top stories.
                
                CNN
                 — 
                Viewing the aftermath of Hurricane Ida from the air, US Coast Guard Vice Adm. Steven Poulin saw utter devastation.
                
                “It was catastrophic,” he told CNN’s “Erin Burnett OutFront.” “My heart breaks for the people in Louisiana.”
                
                Poulin said the pictures he’s seen in the media don’t reveal the extent of the storm’s wrath.
                
                TRACK IDA’S PATH
                
                Officials have said at least two people died because of Sunday’s Category 4 hurricane, but that’s a number the governor said he expects will rise.
                
                In Louisiana on the day after the storm, some neighborhoods were underwater and many streets were full of debris.
                
                More than 1 million homes and businesses were without power, and that could be the case for many residents for days or weeks.
                
                18 hurricane ida 0830
                RELATED ARTICLE
                How you can help Hurricane Ida victims
                And Ida, now a slow-moving tropical depression over Mississippi, is still threatening to flood communities not just in the Deep South but also into the Tennessee and Ohio valleys as it crawls north over the next few days.
                
                Sign up for email updates for significant storms
                
                In Louisiana, rescue and recovery efforts were well underway in parts of the state where Ida slogged through for hours as a Category 4 hurricane.
                
                In St. John the Baptist Parish, where 17 inches of rain and 5 feet of storm surge flooded the area just northwest of New Orleans, nearly 800 people had to be rescued Monday.
                
                “This is one of the worst natural disasters I’ve ever seen in St. John,” parish President Jaclyn Hotard said.
                
                While damage was immense, there were no reports of any deaths, she said.
                '''
            },
            {
                'link': 'https://www.cnn.com/2021/08/30/weather/hurricane-ida-monday/index.html',
                'title': 'Hurricane Ida: New Orleans braces for Category 4 storm',
                'published_at': '2021-08-30T12:00:00Z',
                'has_entities': True,
                'entities': {
                    'date_time': [
                        '4am',
                    ],
                    'entities': [
                        'Hurricane Ida',
                    ],
                },
            },
        ]
    },
    'non-informative': {
        'id': 2,
        'text': 'I love my dog so much. He is my best friend. #doglovers #mansbestfriend',
        'label': 'Non-Informative',
        'confidence': 0.99,
        'entities': {},
        'links': [],
    },
}
