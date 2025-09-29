# Context Aware Translator (Prototype)

Offline(ish) multilingual speech → text → translated text pipeline with:
- Whisper (on-device ASR via `whisper.cpp`)
- (Planned) VAD (Silero / WebRTC)
- (Planned) Context / glossary injection
- (Planned) Embeddings for disambiguation
- (Planned) Adaptive power modes

## Modules
- app: UI (Jetpack Compose)
- core-audio: Audio capture & VAD interface
- core-asr: Whisper integration (JNI) + session manager
- core-translation: Translation abstraction (initial: stub / ML Kit)
- core-context: Conversation state, glossary
- core-model: Model download & registry
- core-eval: Instrumentation and metrics

## Quick Start

```bash
git clone --recurse-submodules git@github.com:patricklarocque1/ContextAwareTranslator.git
cd ContextAwareTranslator
# If you forgot --recurse-submodules:
git submodule update --init --recursive
```

Open in Android Studio (Arctic Fox+). Allow it to install NDK / CMake.

Run the app: choose a model to download, then tap the microphone.

## Model Handling
The app fetches Whisper GGUF model files from a configurable base URL (`model_sources.json`). You can host quantized models on e.g. Hugging Face or your own CDN.

## Whisper Notes
Uses `whisper.cpp` (MIT). JNI layer wraps:
- Model load
- Streaming segment decode
- Partial + final callback

## To Do
See `docs/ROADMAP.md` (or create issues).

## Licensing
- Core app: MIT
- `external/whisper.cpp`: MIT (submodule)
- Be cautious adding models with non-commercial licenses.

## Security / Privacy
User audio is processed locally. (If you add cloud fallback, document data handling.)
