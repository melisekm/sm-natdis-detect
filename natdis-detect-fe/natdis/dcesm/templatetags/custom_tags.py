from django.conf import settings
from django import template
register = template.Library()


# @register.simple_tag
# def capitalize(string=None):
#     return string.capitalize()


# @register.simple_tag
# def format_datetime(datetime):
#     parsed_datetime = parser.parse(datetime)
#     return parsed_datetime.strftime('%d/%m/%Y, %H:%M:%S')


# @register.simple_tag(takes_context=True)
# def logged_user(context):
#     return context.request.session['user']
