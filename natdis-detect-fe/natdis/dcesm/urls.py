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
        views.CreatePrediction.as_view(),
        name='predict'
    ),
    path(
        'predictions',
        views.Predictions.as_view(),
        name='predictions'
    ),
    path(
        'predictions/<int:prediction_id>',
        views.PredictionDetail.as_view(),
        name='prediction_detail'
    ),
    path(
        'rate_prediction/',
        views.RatePrediction.as_view(),
        name='rate_prediction'
    ),
    path(
        'extract_entities/',
        views.CreateEntityAnnotation.as_view(),
        name='extract_entities'
    ),
    path(
        'logout',
        views.Logout.as_view(),
        name='logout'
    ),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
