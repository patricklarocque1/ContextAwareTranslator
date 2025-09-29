# Roadmap

## Milestone 0 (Boot)
- [ ] Build & run scaffold
- [ ] Download tiny model
- [ ] Transcribe short phrase offline

## Milestone 1 (Streaming + Translation)
- [ ] Implement streaming queue
- [ ] Real-time partial transcripts
- [ ] Integrate ML Kit translation
- [ ] Basic UI improvements

## Milestone 2 (VAD + Power)
- [ ] Integrate Silero or WebRTC VAD
- [ ] Gated ASR activation
- [ ] Battery metrics instrumentation

## Milestone 3 (Context & Glossary)
- [ ] Conversation rolling store
- [ ] Glossary injection post-process
- [ ] Simple disambiguation heuristics

## Milestone 4 (Adaptive Models)
- [ ] Tiny for live partials
- [ ] Small for refine pass
- [ ] Confidence-based re-run

## Milestone 5 (Embeddings)
- [ ] Add embedding model (MiniLM ONNX)
- [ ] Vector store (SQLite + FTS5 or lite index)
- [ ] Coreference heuristics

## Milestone 6 (Polish)
- [ ] Settings screen
- [ ] Model management (delete, space usage)
- [ ] Export transcripts (encrypted)
