from django.conf import settings
from django.conf.urls.static import static
from django.urls import path

from . import views

urlpatterns = [
    path('', views.Homepage.as_view(), name='homepage'),

    path(
        'login',
        views.Login.as_view(),
        name='login'
    ),

    path(
        'predict/',
        views.Predict.as_view(),
        name='predict'
    ),
    path(
        'entities',
        views.Entities.as_view(),
        name='entities'
    ),
    path(
        'predictions',
        views.Predictions.as_view(),
        name='predictions'
    ),
    path(
        'logout',
        views.Logout.as_view(),
        name='logout'
    ),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
