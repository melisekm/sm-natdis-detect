import logging
import os
import pathlib
from importlib import reload

os.environ["BASE_DIR"] = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

data_dir = pathlib.Path(os.getenv("DATA_DIR", "data"))
if data_dir.is_absolute():
    DATA_DIR_PATH = data_dir
else:
    DATA_DIR_PATH = pathlib.Path(os.environ["BASE_DIR"]) / data_dir

MODEL_DIR = DATA_DIR_PATH / "models"
XDG_CACHE_HOME = DATA_DIR_PATH / ".cache"
os.environ["XDG_CACHE_HOME"] = str(XDG_CACHE_HOME)

# set correct paths for transformers cache
import transformers.utils.hub as hf_hub

reload(hf_hub)

logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] {%(filename)s:%(lineno)d} %(levelname)s - %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
)


def get_path(type_: str, filename: str, raise_on_not_found=False) -> pathlib.Path:
    if type_ == "data":
        path_folder = DATA_DIR_PATH
    elif type_ == "models":
        path_folder = MODEL_DIR
    elif type_ == 'cache':
        path_folder = XDG_CACHE_HOME
    else:
        raise ValueError(f"Invalid dataset type: {type_}")

    path = path_folder / filename
    if raise_on_not_found and not path.exists():
        raise FileNotFoundError(f"File {path} not found.")
    return path
